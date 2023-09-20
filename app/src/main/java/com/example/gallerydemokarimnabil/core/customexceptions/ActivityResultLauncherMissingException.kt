package com.example.gallerydemokarimnabil.core.customexceptions

class ActivityResultLauncherMissingException : Exception() {
    override val message = "The ActivityResultLauncher<Array<String>> representing " +
            "a strings array of permissions is missing"
}