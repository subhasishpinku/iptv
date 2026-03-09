package com.bacbpl.iptv.ui.activities.subscribescreen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bacbpl.iptv.ui.activities.subscribescreen.data.Plan
import com.bacbpl.iptv.ui.activities.subscribescreen.data.SubscribePlanResponse
import com.bacbpl.iptv.ui.activities.subscribescreen.data.repositories.PlanRepository
import kotlinx.coroutines.launch
import java.io.IOException

class PlanViewModel : ViewModel() {

    private val repository = PlanRepository()

    private val _monthlyPlans = MutableLiveData<List<Plan>>()
    val monthlyPlans: LiveData<List<Plan>> = _monthlyPlans

    private val _quarterlyPlans = MutableLiveData<List<Plan>>()
    val quarterlyPlans: LiveData<List<Plan>> = _quarterlyPlans

    private val _halfYearlyPlans = MutableLiveData<List<Plan>>()
    val halfYearlyPlans: LiveData<List<Plan>> = _halfYearlyPlans

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _selectedPlan = MutableLiveData<Plan?>()
    val selectedPlan: LiveData<Plan?> = _selectedPlan

    // New LiveData for subscription response
    private val _subscribeResponse = MutableLiveData<SubscribePlanResponse?>()
    val subscribeResponse: LiveData<SubscribePlanResponse?> = _subscribeResponse

    private val _isSubscribing = MutableLiveData<Boolean>()
    val isSubscribing: LiveData<Boolean> = _isSubscribing

    init {
        loadAllPlans()
    }

    fun loadAllPlans() {
        loadMonthlyPlans()
        loadQuarterlyPlans()
        loadHalfYearlyPlans()
    }

    fun loadMonthlyPlans() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getPlansByDuration(PlanRepository.DURATION_MONTHLY)
                if (response.isSuccessful) {
                    response.body()?.data?.let { plans ->
                        _monthlyPlans.value = plans
                    }
                } else {
                    _errorMessage.value = "Error: ${response.code()}"
                }
            } catch (e: IOException) {
                _errorMessage.value = "Network error: ${e.message}"
            } catch (e: Exception) {
                _errorMessage.value = "Unexpected error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadQuarterlyPlans() {
        viewModelScope.launch {
            try {
                val response = repository.getPlansByDuration(PlanRepository.DURATION_QUARTERLY)
                if (response.isSuccessful) {
                    response.body()?.data?.let { plans ->
                        _quarterlyPlans.value = plans
                    }
                }
            } catch (e: Exception) {
                // Handle error silently or log
            }
        }
    }

    fun loadHalfYearlyPlans() {
        viewModelScope.launch {
            try {
                val response = repository.getPlansByDuration(PlanRepository.DURATION_HALF_YEARLY)
                if (response.isSuccessful) {
                    response.body()?.data?.let { plans ->
                        _halfYearlyPlans.value = plans
                    }
                }
            } catch (e: Exception) {
                // Handle error silently or log
            }
        }
    }

    fun selectPlan(plan: Plan) {
        _selectedPlan.value = plan
    }

    fun clearSelection() {
        _selectedPlan.value = null
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun clearSubscribeResponse() {
        _subscribeResponse.value = null
    }

    fun getPlansForDuration(duration: Int): LiveData<List<Plan>> {
        return when (duration) {
            PlanRepository.DURATION_MONTHLY -> monthlyPlans
            PlanRepository.DURATION_QUARTERLY -> quarterlyPlans
            PlanRepository.DURATION_HALF_YEARLY -> halfYearlyPlans
            else -> monthlyPlans
        }
    }

    // New function to subscribe to a plan
    fun subscribeToPlan(mobile: String, planCode: Int) {
        viewModelScope.launch {
            _isSubscribing.value = true
            _subscribeResponse.value = null

            try {
                val response = repository.subscribePlan(mobile, planCode)
                if (response.isSuccessful) {
                    _subscribeResponse.value = response.body()
                    println("Subscription : ${response.body()}");
                } else {
                    _errorMessage.value = "Subscription failed: ${response.code()}"
                    println("Subscription : ${response.body()} ${response.code()}");

                }
            } catch (e: IOException) {
                _errorMessage.value = "Network error: ${e.message}"
            } catch (e: Exception) {
                _errorMessage.value = "Unexpected error: ${e.message}"
            } finally {
                _isSubscribing.value = false
            }
        }
    }
}