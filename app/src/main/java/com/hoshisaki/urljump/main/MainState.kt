package com.hoshisaki.urljump.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoshisaki.urljump.R
import com.hoshisaki.urljump.main.view.Param
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainUiState(
    var hostText: String = "",
    var routeText: String = "",
    val params: SnapshotStateList<Param> = mutableStateListOf(Param())
)

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    // host text input's onTextChange callback
    fun onHostTextChange(text: String) {
        _uiState.update { currentState ->
            currentState.copy(
                hostText = text
            )
        }
    }

    // route text input's onTextChange callback
    fun onRouteTextChange(text: String) {
        _uiState.update { currentState ->
            currentState.copy(
                routeText = text
            )
        }
    }

    fun onParamKeyChange(index: Int, text: String) {
        viewModelScope.launch {
            val param = _uiState.value.params[index]
            _uiState.value.params[index] = param.copy(key = text)
            determineRemoveSpace(param = _uiState.value.params[index])
        }
    }

    fun onParamValueChange(index: Int, text: String) {
        viewModelScope.launch {
            val param = _uiState.value.params[index]
            _uiState.value.params[index] = param.copy(value = text)
            determineRemoveSpace(param = _uiState.value.params[index])
        }
    }

    private fun determineRemoveSpace(param: Param) {
        var emptyCount = 0
        for (p: Param in _uiState.value.params) {
            if (TextUtils.isEmpty(p.key) && TextUtils.isEmpty(p.value)) {
                emptyCount += 1
            }
        }
        if (emptyCount <= 1) {
            return
        }
        if (TextUtils.isEmpty(param.key) && TextUtils.isEmpty(param.value)) {
            _uiState.value.params.remove(param)
        }
    }

    fun addNewParam() {
        viewModelScope.launch {
            _uiState.value.params.add(Param())
        }
    }

    fun startIntent(context: Context) {
        val url: String? = generateURL()
        if (url == null) {
            Toast.makeText(
                context,
                context.getString(R.string.url_invalid_toast),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
        context.startActivity(intent)
    }

    private fun generateURL(): String? {
        val urlBuffer = StringBuffer()
        val hostText = _uiState.value.hostText
        if (!TextUtils.isEmpty(hostText)) {
            urlBuffer.append(hostText)
        } else {
            return null
        }
        val routeText = _uiState.value.routeText
        if (!TextUtils.isEmpty(routeText)) {
            urlBuffer.append("/$routeText")
        }
        val paramBuffer = StringBuffer()
        for (param in _uiState.value.params) {
            // if is first query param, add the '?' symbol
            if (paramBuffer.isEmpty()) {
                paramBuffer.append("?")
            } else {
                // else, add a connector '&'
                paramBuffer.append('&')
            }
            paramBuffer
                .append(param.key)
                .append("=")
                .append(param.value)
        }
        urlBuffer.append(paramBuffer)
        return urlBuffer.toString()
    }
}