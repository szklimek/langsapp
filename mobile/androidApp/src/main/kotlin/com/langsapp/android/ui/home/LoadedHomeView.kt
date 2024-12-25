package com.langsapp.android.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    Column {
        val userProfileInfo = state.userProfileInfo
        val onBoardingInfo = state.onBoardingInfo

        TopAppBar(
            title = {
                Text(stringResource(R.string.toolbar_title_home))
            }
        )
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
                .padding(16.dp)
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

        Spacer(Modifier.height(8.dp))

        ElevatedCard(
            elevation = CardDefaults.elevatedCardElevation(),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            if (onBoardingInfo is CommonResult.Success) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "On-boarding completed: ${onBoardingInfo.value.isCompleted}",
                        modifier = Modifier
                            .padding(vertical = 16.dp, horizontal = 8.dp)
                            .weight(3f)
                    )
                    if (onBoardingInfo.value.areRequiredStepsDone) {
                        TextButton(onClick =
                        {

                        }, modifier = Modifier
                            .weight(1f)) {
                            Text(text = "Collapse")
                        }
                    }
                }
                onBoardingInfo.value.sections.forEachIndexed { index, section ->
                    Spacer(Modifier.height(8.dp))
                    StepInfoView2(actionSender, section.rootStep)
                    section.childSteps.forEach { stepInfo -> StepInfoView2(actionSender, stepInfo) }
                }
            } else {
                Text("Failed to load on-boarding info")
            }
        }
    }
}

@Composable
fun OnboardingSectionCard(
    actionSender: ActionSender<Action>,
    stepInfoList: List<OnBoardingInfo.StepInfo>
) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column {
            stepInfoList.forEach { stepInfo ->
                Text(
                    text = when (stepInfo.step) {
                        OnBoardingInfo.OnBoardingStep.SELECT_LANGUAGES -> "Select languages you want to learn"
                        OnBoardingInfo.OnBoardingStep.SIGN_UP -> "Create account and use all Langsapp features"
                        OnBoardingInfo.OnBoardingStep.FILL_PROFILE -> "Fill your profile information so that other users can get to know you"
                        OnBoardingInfo.OnBoardingStep.DOWNLOAD_CONTENT -> "Download content that you want to start learning"
                    },
                    modifier = Modifier.padding(8.dp)
                )
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
    }

}

@Composable
fun StepInfoView2(
    actionSender: ActionSender<Action>,
    stepInfo: OnBoardingInfo.StepInfo
) {
    Column {
        Text(
            text = when (stepInfo.step) {
                OnBoardingInfo.OnBoardingStep.SELECT_LANGUAGES -> "Select languages you want to learn"
                OnBoardingInfo.OnBoardingStep.SIGN_UP -> "Create account and use all Langsapp features"
                OnBoardingInfo.OnBoardingStep.FILL_PROFILE -> "Fill your profile information so that other users can get to know you"
                OnBoardingInfo.OnBoardingStep.DOWNLOAD_CONTENT -> "Download content that you want to start learning"
            },
            modifier = Modifier.padding(8.dp)
        )
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

@Composable
internal fun StepInfoView(
    actionSender: ActionSender<Action>,
    stepInfo: OnBoardingInfo.StepInfo,
) {
    Text("${stepInfo.step}, required: ${stepInfo.required}, done: ${stepInfo.done}, enabled: ${stepInfo.enabled}")
    if (!stepInfo.done) {
        Button(
            onClick = {
                when (stepInfo.step) {
                    OnBoardingInfo.OnBoardingStep.SELECT_LANGUAGES -> actionSender.sendAction(
                        HomeAction.SelectLanguagesTapped
                    )

                    OnBoardingInfo.OnBoardingStep.SIGN_UP -> actionSender.sendAction(HomeAction.SignUpTapped)
                    OnBoardingInfo.OnBoardingStep.FILL_PROFILE -> actionSender.sendAction(HomeAction.UpsertProfileTapped)
                    OnBoardingInfo.OnBoardingStep.DOWNLOAD_CONTENT -> actionSender.sendAction(
                        HomeAction.DownloadContentTapped
                    )
                }
            },
            enabled = stepInfo.enabled,
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
