package com.example.nba_list.di

import com.example.nba_list.data.network.API_KEY
import com.example.nba_list.data.network.ApiService
import com.example.nba_list.data.network.RetrofitClient
import com.example.nba_list.data.repositories.PlayersRepository
import com.example.nba_list.data.repositories.PlayersRepositoryImpl
import com.example.nba_list.vm.PlayerListViewModel
import com.example.nba_list.vm.TopBarViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single<ApiService> {
        RetrofitClient(apiKey = API_KEY).createClient().create(ApiService::class.java)
    }
}

val repoModule = module{
    includes(networkModule)
    single<PlayersRepository> { PlayersRepositoryImpl(get()) }
}

val vmModule = module{
    includes(repoModule)
    viewModel { PlayerListViewModel(get()) }
    viewModel { TopBarViewModel() }
}

val appModule = module {
    includes(vmModule)
}