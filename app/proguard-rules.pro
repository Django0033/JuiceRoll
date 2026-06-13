# ─────────────────────────────────────────────────────────────
# JuiceRoll ProGuard / R8 Rules
# ─────────────────────────────────────────────────────────────

# ── Kotlin Serialization ────────────────────────────────────
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class com.juiceroll.**$$serializer { *; }
-keepclassmembers class com.juiceroll.** {
    *** Companion;
}
-keepclasseswithmembers class com.juiceroll.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# ── Hilt / Dagger ───────────────────────────────────────────
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# ── Jetpack Compose ─────────────────────────────────────────
-keep class androidx.compose.** { *; }

# ── Kotlin Metadata ─────────────────────────────────────────
-keep class kotlin.Metadata { *; }

# ── Domain / Data model classes (serialization) ─────────────
-keep class com.juiceroll.domain.model.** { *; }
-keep class com.juiceroll.data.** { *; }
