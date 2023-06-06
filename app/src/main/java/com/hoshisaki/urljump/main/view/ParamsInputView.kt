package com.hoshisaki.urljump.main.view

import android.text.TextUtils
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hoshisaki.urljump.R

@Composable
fun ParamsInputView(
    onStartClick: () -> Unit,
    params: MutableList<Param>,
    onKeyChange: (Int, String) -> Unit,
    onValueChange: (Int, String) -> Unit,
    onAddClick: () -> Unit,
) {
    var tableAddButtonEnable = true
    for (param: Param in params) {
        if (TextUtils.isEmpty(param.key) && TextUtils.isEmpty(param.value)) {
            tableAddButtonEnable = false
        }
    }
    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 10.dp)
            .padding(top = 6.dp)
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.params_input)
            )
            Table(
                tableAddButtonEnable = tableAddButtonEnable,
                onAddClick = onAddClick
            ) {
                // Param input list
                for (index in params.indices) {
                    val param = params[index]
                    TableRow(
                        key = param.key,
                        value = param.value,
                        onKeyChange = { value -> onKeyChange(index, value) },
                        onValueChange = { value -> onValueChange(index, value) },
                    )
                }
            }
            StartButton(onStartClick = { onStartClick() })
        }
    }
}

@Composable
private fun StartButton(onStartClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
    ) {
        Button(
            onClick = { onStartClick() },
            modifier = Modifier.align(
                alignment = Alignment.Center
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.PlayArrow,
                    contentDescription = stringResource(
                        id = R.string.start_button,
                    )
                )
                Box(
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Text(stringResource(id = R.string.start_button))
                }
            }
        }
    }
}

data class Param(var key: String = "", var value: String = "")

@Composable
private fun Table(
    tableAddButtonEnable: Boolean,
    onAddClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.padding(top = 12.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
            TableAddButton(enabled = tableAddButtonEnable, onClick = onAddClick)
        }
    }
}

@Composable
private fun TableAddButton(
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .clickable(onClick = { if (enabled) onClick() })
            .padding(all = 12.dp)
    ) {
        Icon(
            modifier = Modifier
                .padding(all = 4.dp)
                .width(width = 24.dp)
                .height(height = 24.dp),
            imageVector = Icons.Rounded.Add,
            tint = when (enabled) {
                true -> LocalContentColor.current
                false -> MaterialTheme.colorScheme.primaryContainer
            },
            contentDescription = stringResource(
                id = R.string.start_button,
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TableRow(
    key: String,
    value: String,
    onKeyChange: (String) -> Unit,
    onValueChange: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .height(54.dp)
            .fillMaxWidth()
    ) {
        TextField(
            modifier = Modifier
                .weight(weight = 1f)
                .wrapContentWidth(Alignment.Start),
            value = key,
            onValueChange = { value -> onKeyChange(value) },
            placeholder = { Text(stringResource(id = R.string.key)) }
        )
        Divider(
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier
                .fillMaxHeight()  //fill the max height
                .width(1.dp)
        )
        TextField(
            modifier = Modifier
                .weight(weight = 2f)
                .wrapContentWidth(Alignment.Start),
            value = value,
            onValueChange = { value -> onValueChange(value) },
            placeholder = { Text(stringResource(id = R.string.value)) }
        )
    }
}

