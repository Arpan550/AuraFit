package com.example.aurafit.ui.authentication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aurafit.R

class WelcomeViewModel : ViewModel() {
    private val _currentIndex = MutableLiveData<Int>(0)
    val currentIndex: LiveData<Int> get() = _currentIndex

    private val _imageId = MutableLiveData<Int>()
    val imageId: LiveData<Int> get() = _imageId

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> get() = _text

    val imageIds = arrayOf(
        R.drawable.welcomee,
        R.drawable.mindfulness,
        R.drawable.progress,
        R.drawable.boost,
        R.drawable.community
    )
    private val texts = arrayOf(
        "Welcome to AuraFit, your companion in physical and mental wellness.",
        "Start your day with mindful exercises and positive affirmations.",
        "Track your progress and achieve your health goals with ease.",
        "Discover a range of activities to boost your physical and mental health.",
        "Join our community and stay motivated on your wellness journey."
    )

    init {
        // Load the first image and text initially
        loadNextImageAndText()
    }

    fun loadNextImageAndText() {
        val index = _currentIndex.value ?: 0
        if (index < imageIds.size) {
            _imageId.value = imageIds[index]
            _text.value = texts[index]
            _currentIndex.value = index + 1
        }
    }

    fun getProgress(): Int {
        val index = _currentIndex.value?.minus(1) ?: 0
        return ((index + 1) * 100) / imageIds.size
    }
}
