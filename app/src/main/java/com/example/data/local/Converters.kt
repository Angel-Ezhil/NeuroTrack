package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.AssessmentStatus
import com.example.data.model.AssessmentType

class Converters {
    @TypeConverter
    fun fromAssessmentStatus(status: AssessmentStatus): String = status.name

    @TypeConverter
    fun toAssessmentStatus(value: String): AssessmentStatus = runCatching {
        AssessmentStatus.valueOf(value)
    }.getOrDefault(AssessmentStatus.WITHIN_BASELINE)

    @TypeConverter
    fun fromAssessmentType(type: AssessmentType): String = type.name

    @TypeConverter
    fun toAssessmentType(value: String): AssessmentType = runCatching {
        AssessmentType.valueOf(value)
    }.getOrDefault(AssessmentType.BEFORE_AFTER)
}
