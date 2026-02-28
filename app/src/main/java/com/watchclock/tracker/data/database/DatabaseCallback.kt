package com.watchclock.tracker.data.database

import com.watchclock.tracker.data.model.Brand
import com.watchclock.tracker.data.model.CollectionType

class DatabaseCallback(private val database: AppDatabase) {

    suspend fun populateDefaultBrands() {
        val brandDao = database.brandDao()
        if (brandDao.getDefaultBrandCount() > 0) return

        val watchBrands = listOf(
            Brand(name = "Rolex", commonMisspellings = "[\"Rollex\",\"Rolax\"]", country = "Switzerland", foundedYear = 1905, type = CollectionType.WATCH),
            Brand(name = "Omega", commonMisspellings = "[\"Omaga\",\"Omego\"]", country = "Switzerland", foundedYear = 1848, type = CollectionType.WATCH),
            Brand(name = "Patek Philippe", commonMisspellings = "[\"Patek Philip\",\"Pattek Philippe\"]", country = "Switzerland", foundedYear = 1839, type = CollectionType.WATCH),
            Brand(name = "Audemars Piguet", commonMisspellings = "[\"Audemar Piguet\",\"Audemars Piquet\"]", country = "Switzerland", foundedYear = 1875, type = CollectionType.WATCH),
            Brand(name = "Vacheron Constantin", commonMisspellings = "[\"Vacheron Constatin\",\"Vacheron Contsantin\"]", country = "Switzerland", foundedYear = 1755, type = CollectionType.WATCH),
            Brand(name = "Jaeger-LeCoultre", commonMisspellings = "[\"Jaeger LeCoultre\",\"Jaeger Le Coultre\"]", country = "Switzerland", foundedYear = 1833, type = CollectionType.WATCH),
            Brand(name = "IWC", commonMisspellings = "[\"IWC Schaffhausen\",\"I.W.C\"]", country = "Switzerland", foundedYear = 1868, type = CollectionType.WATCH),
            Brand(name = "Cartier", commonMisspellings = "[\"Carteer\",\"Cartear\"]", country = "France", foundedYear = 1847, type = CollectionType.WATCH),
            Brand(name = "Breguet", commonMisspellings = "[\"Bregnet\",\"Bregat\"]", country = "Switzerland", foundedYear = 1775, type = CollectionType.WATCH),
            Brand(name = "Blancpain", commonMisspellings = "[\"Blancpan\",\"Blanc Pain\"]", country = "Switzerland", foundedYear = 1735, type = CollectionType.WATCH),
            Brand(name = "A. Lange & Söhne", commonMisspellings = "[\"A Lange Sohne\",\"Lange & Sohne\"]", country = "Germany", foundedYear = 1845, type = CollectionType.WATCH),
            Brand(name = "Girard-Perregaux", commonMisspellings = "[\"Girard Perregaux\",\"Gerard Perregaux\"]", country = "Switzerland", foundedYear = 1791, type = CollectionType.WATCH),
            Brand(name = "Zenith", commonMisspellings = "[\"Zeneth\",\"Zenith Watch\"]", country = "Switzerland", foundedYear = 1865, type = CollectionType.WATCH),
            Brand(name = "TAG Heuer", commonMisspellings = "[\"Tag Heuer\",\"Tag Huer\",\"Tagheuer\"]", country = "Switzerland", foundedYear = 1860, type = CollectionType.WATCH),
            Brand(name = "Breitling", commonMisspellings = "[\"Brietling\",\"Bretling\"]", country = "Switzerland", foundedYear = 1884, type = CollectionType.WATCH),
            Brand(name = "Seiko", commonMisspellings = "[\"Saiko\",\"Sieко\"]", country = "Japan", foundedYear = 1881, type = CollectionType.WATCH),
            Brand(name = "Citizen", commonMisspellings = "[\"Citezen\",\"Citisen\"]", country = "Japan", foundedYear = 1918, type = CollectionType.WATCH),
            Brand(name = "Hamilton", commonMisspellings = "[\"Hamiltion\",\"Hamiltom\"]", country = "United States", foundedYear = 1892, type = CollectionType.WATCH),
            Brand(name = "Longines", commonMisspellings = "[\"Longins\",\"Longines Watch\"]", country = "Switzerland", foundedYear = 1832, type = CollectionType.WATCH),
            Brand(name = "Tissot", commonMisspellings = "[\"Tiso\",\"Tisot\"]", country = "Switzerland", foundedYear = 1853, type = CollectionType.WATCH),
            Brand(name = "Bulova", commonMisspellings = "[\"Bulvoa\",\"Buolva\"]", country = "United States", foundedYear = 1875, type = CollectionType.WATCH),
            Brand(name = "Timex", commonMisspellings = "[\"Timeks\",\"Tymex\"]", country = "United States", foundedYear = 1854, type = CollectionType.WATCH),
            Brand(name = "Elgin", commonMisspellings = "[\"Elgen\",\"Elgan\"]", country = "United States", foundedYear = 1864, type = CollectionType.WATCH),
            Brand(name = "Waltham", commonMisspellings = "[\"Waltam\",\"Walthem\"]", country = "United States", foundedYear = 1850, type = CollectionType.WATCH),
            Brand(name = "Illinois", commonMisspellings = "[\"Ilinois\",\"Illinios\"]", country = "United States", foundedYear = 1869, type = CollectionType.WATCH)
        )

        val clockBrands = listOf(
            Brand(name = "Seth Thomas", commonMisspellings = "[\"Seth Tomas\",\"Seth Thomas Clock\"]", country = "United States", foundedYear = 1813, type = CollectionType.CLOCK),
            Brand(name = "Ansonia", commonMisspellings = "[\"Ansona\",\"Ansonya\"]", country = "United States", foundedYear = 1850, type = CollectionType.CLOCK),
            Brand(name = "Waterbury", commonMisspellings = "[\"Waterberry\",\"Waterburey\"]", country = "United States", foundedYear = 1857, type = CollectionType.CLOCK),
            Brand(name = "New Haven", commonMisspellings = "[\"Newhaven\",\"New Haeven\"]", country = "United States", foundedYear = 1853, type = CollectionType.CLOCK),
            Brand(name = "Gilbert", commonMisspellings = "[\"Gilburt\",\"Gilbart\"]", country = "United States", foundedYear = 1807, type = CollectionType.CLOCK),
            Brand(name = "Howard Miller", commonMisspellings = "[\"Howard Millar\",\"Howord Miller\"]", country = "United States", foundedYear = 1926, type = CollectionType.CLOCK),
            Brand(name = "Ridgeway", commonMisspellings = "[\"Ridgway\",\"Ridgeway Clocks\"]", country = "United States", foundedYear = 1926, type = CollectionType.CLOCK),
            Brand(name = "Hermle", commonMisspellings = "[\"Hermal\",\"Hermale\"]", country = "Germany", foundedYear = 1922, type = CollectionType.CLOCK),
            Brand(name = "Kieninger", commonMisspellings = "[\"Kieniger\",\"Kienenger\"]", country = "Germany", foundedYear = 1912, type = CollectionType.CLOCK),
            Brand(name = "Urgos", commonMisspellings = "[\"Urgus\",\"Urgoss\"]", country = "Germany", foundedYear = 1914, type = CollectionType.CLOCK),
            Brand(name = "Comtoise", commonMisspellings = "[\"Comtois\",\"Comtoise Clock\"]", country = "France", foundedYear = null, type = CollectionType.CLOCK),
            Brand(name = "Morbier", commonMisspellings = "[\"Morbear\",\"Morbiar\"]", country = "France", foundedYear = null, type = CollectionType.CLOCK),
            Brand(name = "Gustav Becker", commonMisspellings = "[\"Gustave Becker\",\"Gustav Beker\"]", country = "Germany", foundedYear = 1847, type = CollectionType.CLOCK),
            Brand(name = "Junghans", commonMisspellings = "[\"Junghanns\",\"Yungans\"]", country = "Germany", foundedYear = 1861, type = CollectionType.CLOCK),
            Brand(name = "Kienzle", commonMisspellings = "[\"Keinzle\",\"Kiensle\"]", country = "Germany", foundedYear = 1882, type = CollectionType.CLOCK)
        )

        brandDao.insertBrands(watchBrands)
        brandDao.insertBrands(clockBrands)
    }
}
