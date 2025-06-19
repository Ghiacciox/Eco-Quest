package com.ingegneriasoftware.ecoquest.di

import android.content.Context
import com.ingegneriasoftware.ecoquest.SupabaseClient
import com.ingegneriasoftware.ecoquest.data.repositories.AuthRepository
import com.ingegneriasoftware.ecoquest.data.repositories.LeaderboardRepository
import com.ingegneriasoftware.ecoquest.data.repositories.MissionsRepository
import com.ingegneriasoftware.ecoquest.data.repositories.ProfileRepository
import com.ingegneriasoftware.ecoquest.data.repositories.TrophyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Questo modulo fornisce le dipendenze per i repository e il SupabaseClient utilizzando Hilt.
 * Hilt è un framework per la Dependency Injection che semplifica la gestione delle dipendenze.
 */
@Module
@InstallIn(SingletonComponent::class) // Il modulo è attivo per l'intera applicazione.
object AppModule {

    /**
     * Fornisce un'istanza singleton di SupabaseClient.
     */
    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return SupabaseClient() // Crea un'istanza di SupabaseClient.
    }

    /**
     * Fornisce un'istanza singleton di MissionsRepository.
     * @param supabaseClient Il client Supabase necessario per il repository.
     */
    @Provides
    @Singleton
    fun provideMissionsRepository(supabaseClient: SupabaseClient): MissionsRepository {
        return MissionsRepository(supabaseClient) // Passa SupabaseClient al repository.
    }

    /**
     * Fornisce un'istanza singleton di ProfileRepository.
     * @param supabaseClient Il client Supabase necessario per il repository.
     */
    @Provides
    @Singleton
    fun provideProfileRepository(supabaseClient: SupabaseClient): ProfileRepository {
        return ProfileRepository(supabaseClient) // Passa SupabaseClient al repository.
    }

    /**
     * Fornisce un'istanza singleton di LeaderboardRepository.
     * @param supabaseClient Il client Supabase necessario per il repository.
     */
    @Provides
    @Singleton
    fun provideLeaderboardRepository(supabaseClient: SupabaseClient): LeaderboardRepository {
        return LeaderboardRepository(supabaseClient) // Passa SupabaseClient al repository.
    }

    /**
     * Fornisce un'istanza singleton di TrophyRepository.
     * @param supabaseClient Il client Supabase necessario per il repository.
     */
    @Provides
    @Singleton
    fun provideTrophyRepository(supabaseClient: SupabaseClient): TrophyRepository {
        return TrophyRepository(supabaseClient) // Passa SupabaseClient al repository.
    }

    /**
     * Fornisce un'istanza singleton di AuthRepository.
     * @param supabaseClient Il client Supabase necessario per il repository.
     * @param context Il contesto dell'applicazione necessario per il repository.
     */
    @Provides
    @Singleton
    fun provideAuthRepository(
        supabaseClient: SupabaseClient,
        @ApplicationContext context: Context // Inietta il contesto dell'applicazione.
    ): AuthRepository {
        return AuthRepository(supabaseClient, context) // Passa SupabaseClient e il contesto al repository.
    }
}