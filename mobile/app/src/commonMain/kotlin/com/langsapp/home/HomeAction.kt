package com.langsapp.home

import com.langsapp.architecture.Action

sealed class HomeAction : Action {
    data object SkipTapped : HomeAction()
    data object HomeDataLoaded : HomeAction()
}