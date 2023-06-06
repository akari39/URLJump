package com.hoshisaki.urljump.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hoshisaki.urljump.R
import com.hoshisaki.urljump.main.view.MainInputView
import com.hoshisaki.urljump.main.view.ParamsInputView

@Composable
fun MainPage(
    mainViewModel: MainViewModel = viewModel()
) {
    val mainUiState = mainViewModel.uiState.collectAsState()
    val context = LocalContext.current

    MainScaffold {
        Column {
            /// Host input view
            MainInputView(
                hint = stringResource(id = R.string.host_input),
                value = mainUiState.value.hostText,
                onValueChange = { value -> mainViewModel.onHostTextChange(value) },
                placeholder = stringResource(id = R.string.host_placeholder)
            )
            /// Route input view
            MainInputView(
                hint = stringResource(id = R.string.route_input),
                value = mainUiState.value.routeText,
                onValueChange = { value -> mainViewModel.onRouteTextChange(value) },
                placeholder = stringResource(id = R.string.route_placeholder)
            )
            // Params input view
            ParamsInputView(
                onStartClick = { mainViewModel.startIntent(context = context) },
                params = mainUiState.value.params,
                onKeyChange = { index, value -> mainViewModel.onParamKeyChange(index, value) },
                onValueChange = { index, value -> mainViewModel.onParamValueChange(index, value) },
                onAddClick = { mainViewModel.addNewParam() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScaffold(content: @Composable () -> Unit) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(stringResource(id = R.string.main_title))
                    }
                },
            )
        },
        content = { contentPadding ->
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .imePadding()
                    .background(color = MaterialTheme.colorScheme.surface)
                    .fillMaxHeight()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            focusManager.clearFocus()
                        }
                ) {
                    content()
                }
            }
        }
    )
}