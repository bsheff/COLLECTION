package com.watchclock.tracker.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.*
import com.watchclock.tracker.data.model.*
import com.watchclock.tracker.data.repository.BrandRepository
import com.watchclock.tracker.data.repository.ItemRepository
import com.watchclock.tracker.util.ImageHelper
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class AddEditFormState(
    val type: CollectionType = CollectionType.WATCH,
    val brand: String = "",
    val model: String = "",
    val serialNumber: String = "",
    val yearOfManufacture: String = "",
    val era: String = "",
    val category: String = "",
    val purchaseDate: String = "",
    val purchaseCost: String = "",
    val currentValue: String = "",
    val insuranceValue: String = "",
    val cosmeticCondition: String = "",
    val mechanicalCondition: String = "",
    val movementType: String = "",
    val selectedFeatures: Set<Feature> = emptySet(),
    val repairsNeeded: String = "",
    val notes: String = "",
    val provenance: String = "",
    val location: String = "",
    val tags: String = "",
    val originalDateAdded: Long? = null,
    val pendingPhotoUris: List<Uri> = emptyList(),
    val isSaving: Boolean = false,
    val error: String? = null
)

class AddEditViewModel(
    private val itemRepository: ItemRepository,
    private val brandRepository: BrandRepository,
    private val editItemId: Long?,
    initialType: String
) : ViewModel() {

    private val _form = MutableStateFlow(
        AddEditFormState(type = CollectionType.valueOf(initialType))
    )
    val form: StateFlow<AddEditFormState> = _form.asStateFlow()

    val isEditing: Boolean get() = editItemId != null

    val watchBrands: StateFlow<List<Brand>> = brandRepository.getBrandsByType(CollectionType.WATCH)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val clockBrands: StateFlow<List<Brand>> = brandRepository.getBrandsByType(CollectionType.CLOCK)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        if (editItemId != null) {
            viewModelScope.launch {
                itemRepository.getItemById(editItemId).firstOrNull()?.let { item ->
                    val features = try {
                        Json.decodeFromString<List<String>>(item.features)
                            .mapNotNull { runCatching { Feature.valueOf(it) }.getOrNull() }
                            .toSet()
                    } catch (_: Exception) { emptySet() }

                    _form.value = AddEditFormState(
                        type = item.type,
                        brand = item.brand,
                        model = item.model,
                        serialNumber = item.serialNumber,
                        yearOfManufacture = item.yearOfManufacture?.toString() ?: "",
                        era = item.era ?: "",
                        category = item.category ?: "",
                        purchaseDate = item.purchaseDate ?: "",
                        purchaseCost = item.purchaseCost?.toString() ?: "",
                        currentValue = item.currentValue?.toString() ?: "",
                        insuranceValue = item.insuranceValue?.toString() ?: "",
                        cosmeticCondition = item.cosmeticCondition ?: "",
                        mechanicalCondition = item.mechanicalCondition ?: "",
                        movementType = item.movementType ?: "",
                        selectedFeatures = features,
                        repairsNeeded = item.repairsNeeded ?: "",
                        notes = item.notes ?: "",
                        provenance = item.provenance ?: "",
                        location = item.location ?: "",
                        tags = item.tags ?: "",
                        originalDateAdded = item.dateAdded
                    )
                }
            }
        }
    }

    fun update(block: AddEditFormState.() -> AddEditFormState) {
        _form.value = _form.value.block()
    }

    fun toggleFeature(feature: Feature) {
        val current = _form.value.selectedFeatures
        _form.value = _form.value.copy(
            selectedFeatures = if (feature in current) current - feature else current + feature
        )
    }

    fun saveItem(context: Context, onSuccess: () -> Unit) {
        val f = _form.value
        if (f.brand.isBlank() || f.model.isBlank()) {
            _form.value = f.copy(error = "Brand and Model are required")
            return
        }
        _form.value = f.copy(isSaving = true, error = null)

        viewModelScope.launch {
            val featuresJson = Json.encodeToString(f.selectedFeatures.map { it.name })
            val now = System.currentTimeMillis()

            val item = CollectionItem(
                id = editItemId ?: 0L,
                type = f.type,
                brand = f.brand,
                model = f.model,
                serialNumber = f.serialNumber,
                yearOfManufacture = f.yearOfManufacture.toIntOrNull(),
                era = f.era.ifBlank { null },
                category = f.category.ifBlank { null },
                purchaseDate = f.purchaseDate.ifBlank { null },
                purchaseCost = f.purchaseCost.toDoubleOrNull(),
                currentValue = f.currentValue.toDoubleOrNull(),
                insuranceValue = f.insuranceValue.toDoubleOrNull(),
                cosmeticCondition = f.cosmeticCondition.ifBlank { null },
                mechanicalCondition = f.mechanicalCondition.ifBlank { null },
                movementType = f.movementType.ifBlank { null },
                features = featuresJson,
                repairsNeeded = f.repairsNeeded.ifBlank { null },
                notes = f.notes.ifBlank { null },
                provenance = f.provenance.ifBlank { null },
                location = f.location.ifBlank { null },
                tags = f.tags.ifBlank { null },
                dateAdded = f.originalDateAdded ?: now,
                lastModified = now
            )

            val savedItemId: Long
            if (editItemId != null) {
                itemRepository.updateItem(item)
                savedItemId = editItemId
            } else {
                savedItemId = itemRepository.insertItem(item)
            }

            // Save any pending photos queued during editing
            f.pendingPhotoUris.forEach { uri ->
                val path = ImageHelper.saveAndCompressImage(context, uri, savedItemId)
                if (path != null) {
                    val photo = Photo(itemId = savedItemId, filePath = path)
                    itemRepository.insertPhoto(photo)
                }
            }

            _form.value = _form.value.copy(isSaving = false)
            onSuccess()
        }
    }

    fun addPendingPhoto(uri: Uri) {
        _form.value = _form.value.copy(
            pendingPhotoUris = _form.value.pendingPhotoUris + uri
        )
    }

    fun removePendingPhoto(uri: Uri) {
        _form.value = _form.value.copy(
            pendingPhotoUris = _form.value.pendingPhotoUris - uri
        )
    }

    fun addPhoto(context: Context, uri: Uri, itemId: Long) {
        viewModelScope.launch {
            val path = ImageHelper.saveAndCompressImage(context, uri, itemId) ?: return@launch
            val photo = Photo(itemId = itemId, filePath = path)
            itemRepository.insertPhoto(photo)
        }
    }
}

class AddEditViewModelFactory(
    private val itemRepository: ItemRepository,
    private val brandRepository: BrandRepository,
    private val editItemId: Long?,
    private val initialType: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return AddEditViewModel(itemRepository, brandRepository, editItemId, initialType) as T
    }
}
