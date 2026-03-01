package com.watchclock.tracker.data.model

enum class Feature(val displayName: String) {
    QUARTZ("Quartz"),
    ELECTRIC("Electric"),
    MANUAL_WIND("Manual Wind"),
    AUTOMATIC("Automatic"),
    CALENDAR("Calendar"),
    DAY_DISPLAY("Day Display"),
    DAY_DATE("Day-Date"),
    MOON_PHASE("Moon Phase"),
    CHRONOGRAPH("Chronograph"),
    GMT("GMT"),
    ALARM("Alarm"),
    PERPETUAL_CALENDAR("Perpetual Calendar"),
    MINUTE_REPEATER("Minute Repeater"),
    TOURBILLON("Tourbillon"),
    POWER_RESERVE("Power Reserve"),
    CHIMING_WESTMINSTER("Westminster Chime"),
    CHIMING_WHITTINGTON("Whittington Chime"),
    CHIMING_ST_MICHAEL("St. Michael Chime"),
    STRIKE_HOURLY("Hourly Strike"),
    STRIKE_HALF_HOURLY("Half-Hourly Strike")
}
