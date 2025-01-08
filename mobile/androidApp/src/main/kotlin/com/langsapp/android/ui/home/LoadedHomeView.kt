package com.langsapp.android.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.langsapp.android.app.R
import com.langsapp.architecture.Action
import com.langsapp.architecture.ActionSender
import com.langsapp.common.CommonResult
import com.langsapp.home.HomeAction
import com.langsapp.home.HomeState
import com.langsapp.home.onboarding.OnBoardingInfo
import com.langsapp.home.onboarding.UserProfileInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoadedHomeView(
    actionSender: ActionSender<Action>,
    state: HomeState.Loaded,
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.toolbar_title_home))
                },
                scrollBehavior = scrollBehavior
            )
        },
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .verticalScroll(state = rememberScrollState())
            ) {

                val userProfileInfo = state.userProfileInfo
                val onBoardingInfo = state.onBoardingInfo


//        if (state.devOptionsEnabled) {
//            Button(
//                onClick = {
//                    actionSender.sendAction(HomeAction.DevOptionsTapped)
//                },
//            ) {
//                Text("Dev options")
//            }
//        }
                ElevatedCard(
                    elevation = CardDefaults.elevatedCardElevation(),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (userProfileInfo is CommonResult.Success) {
                            Text(
                                text = "User profile info: $userProfileInfo",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .weight(5f)
                            )
                            if (userProfileInfo.value is UserProfileInfo.Anonymous) {
                                Button(
                                    onClick = {
                                        actionSender.sendAction(HomeAction.SignUpTapped)
                                    }, modifier = Modifier
                                        .weight(3f)
                                        .padding(8.dp)
                                ) {
                                    Text("Sign up")
                                }
                            }
                        } else {
                            Text("Failed to load user profile info")
                        }
                    }

                }

                ElevatedCard(
                    elevation = CardDefaults.elevatedCardElevation(),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    when {
                        onBoardingInfo is CommonResult.Success -> {
                            OnboardingStepsHeadline2(
                                actionSender = actionSender,
                                onBoardingInfo = onBoardingInfo.value,
                                isCollapsed = onBoardingInfo.value.isCollapsed
                            )
                            if (!onBoardingInfo.value.isCollapsed) {
                                onBoardingInfo.value.sections.forEachIndexed { index, section ->
                                    Spacer(Modifier.height(4.dp))
                                    StepInfoView(actionSender, section.rootStep)
                                    section.childSteps.forEach { stepInfo ->
                                        StepInfoView(
                                            actionSender,
                                            stepInfo
                                        )
                                    }
                                }
                            }
                        }

                        else -> Text("Failed to load on-boarding info")
                    }
                }
            }
        })
}

@Composable
fun OnboardingStepsHeadline2(
    actionSender: ActionSender<Action>,
    onBoardingInfo: OnBoardingInfo,
    isCollapsed: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "On-boarding steps: ${onBoardingInfo.getNumberOfStepsCompleted()}/${onBoardingInfo.getNumberOfSteps()}",
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 12.dp)
                .weight(3f),
            style = MaterialTheme.typography.titleMedium
        )
        when {
            isCollapsed -> {
                TextButton(
                    onClick = { actionSender.sendAction(HomeAction.ExpandOnboardingTapped) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(R.string.button_expand))
                }
            }
            onBoardingInfo.areRequiredStepsDone -> {
                TextButton(
                    onClick = { actionSender.sendAction(HomeAction.CollapseOnboardingTapped) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(R.string.button_collapse))
                }
            }
        }
    }
}

@Composable
fun StepInfoView(
    actionSender: ActionSender<Action>,
    stepInfo: OnBoardingInfo.StepInfo
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_checkmark),
                contentDescription = null,
                modifier = Modifier.size(dimensionResource(id = R.dimen.icon_medium)),
                colorFilter = if (stepInfo.done) ColorFilter.tint(color = MaterialTheme.colorScheme.primary) else ColorFilter.tint(
                    color = MaterialTheme.colorScheme.surfaceDim
                )
            )
            Text(
                text = when (stepInfo.step) {
                    OnBoardingInfo.OnBoardingStep.SELECT_LANGUAGES -> "Select languages you want to learn"
                    OnBoardingInfo.OnBoardingStep.SIGN_UP -> "Create account and use all Langsapp features"
                    OnBoardingInfo.OnBoardingStep.FILL_PROFILE -> "Fill your profile information so that other users can get to know you"
                    OnBoardingInfo.OnBoardingStep.DOWNLOAD_CONTENT -> "Download content that you want to start learning"
                },
                modifier = Modifier.padding(horizontal = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        TextButton(
            onClick = {
                when (stepInfo.step) {
                    OnBoardingInfo.OnBoardingStep.SELECT_LANGUAGES -> actionSender.sendAction(
                        HomeAction.SelectLanguagesTapped
                    )

                    OnBoardingInfo.OnBoardingStep.SIGN_UP -> actionSender.sendAction(
                        HomeAction.SignUpTapped
                    )

                    OnBoardingInfo.OnBoardingStep.FILL_PROFILE -> actionSender.sendAction(
                        HomeAction.UpsertProfileTapped
                    )

                    OnBoardingInfo.OnBoardingStep.DOWNLOAD_CONTENT -> actionSender.sendAction(
                        HomeAction.DownloadContentTapped
                    )
                }
            },
            enabled = stepInfo.enabled
        ) {
            when (stepInfo.step) {
                OnBoardingInfo.OnBoardingStep.SELECT_LANGUAGES -> Text("Select languages")
                OnBoardingInfo.OnBoardingStep.SIGN_UP -> Text("Sign up")
                OnBoardingInfo.OnBoardingStep.FILL_PROFILE -> Text("Fill profile")
                OnBoardingInfo.OnBoardingStep.DOWNLOAD_CONTENT -> Text("Download content")
            }
        }
    }
}
