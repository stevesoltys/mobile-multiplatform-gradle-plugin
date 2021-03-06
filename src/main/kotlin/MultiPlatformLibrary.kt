/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.konan.target.Architecture

data class MultiPlatformLibrary(
    val android: String? = null,
    val common: String,
    val iosX64: String? = null,
    val iosArm64: String? = null
) : KotlinNativeExportable {
    constructor(
        android: String? = null,
        common: String,
        ios: String? = null
    ) : this(
        android = android,
        common = common,
        iosX64 = ios,
        iosArm64 = ios
    )

    override fun export(project: Project, framework: Framework) {
        val arch = framework.target.konanTarget.architecture
        when (arch) {
            Architecture.X64 -> iosX64?.let { framework.export(it) }
            Architecture.ARM64 -> iosArm64?.let { framework.export(it) }
            else -> throw IllegalArgumentException("unknown architecture $arch")
        }
    }
}

fun DependencyHandlerScope.mppLibrary(library: MultiPlatformLibrary) {
    library.android?.let { "androidMainApi"(it) }
    "commonMainApi"(library.common)
    library.iosArm64?.let { "iosArm64MainApi"(it) }
    library.iosX64?.let { "iosX64MainApi"(it) }
}

fun DependencyHandlerScope.mppTestLibrary(
    library: MultiPlatformLibrary
) {
    library.android?.let { "androidTestApi"(it) }
    "commonTestApi"(library.common)
    library.iosArm64?.let { "iosArm64TestApi"(it) }
    library.iosX64?.let { "iosX64TestApi"(it) }
}
