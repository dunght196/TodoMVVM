package com.example.todomvvm.di

import android.content.Context
import androidx.room.Room
import com.example.todomvvm.TasksRemoteDataSource
import com.example.todomvvm.data.source.DefaultTasksRepository
import com.example.todomvvm.data.source.TasksDataSource
import com.example.todomvvm.data.source.TasksRepository
import com.example.todomvvm.data.source.local.TasksLocalDataSource
import com.example.todomvvm.data.source.local.ToDoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Module to tell Hilt how to provide instances of types that cannot be constructor-injected.
 *
 * As these types are scoped to the application lifecycle using @Singleton, they're installed
 * in Hilt's ApplicationComponent.
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class RemoteTasksDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalTasksDataSource

    @Singleton
    @RemoteTasksDataSource
    @Provides
    fun provideTasksRemoteDataSource(): TasksDataSource {
        return TasksRemoteDataSource
    }

    @Singleton
    @LocalTasksDataSource
    @Provides
    fun provideTasksLocalDataSource(
        database: ToDoDatabase,
        ioDispatcher: CoroutineDispatcher
    ): TasksDataSource {
        return TasksLocalDataSource(
            database.taskDao(), ioDispatcher
        )
    }

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): ToDoDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ToDoDatabase::class.java,
            "Tasks.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}

/**
 * The binding for TasksRepository is on its own module so that we can replace it easily in tests.
 */
@Module
@InstallIn(SingletonComponent::class)
object TasksRepositoryModule {

    @Singleton
    @Provides
    fun provideTasksRepository(
        @AppModule.RemoteTasksDataSource remoteTasksDataSource: TasksDataSource,
        @AppModule.LocalTasksDataSource localTasksDataSource: TasksDataSource,
        ioDispatcher: CoroutineDispatcher
    ): TasksRepository {
        return DefaultTasksRepository(
            remoteTasksDataSource, localTasksDataSource, ioDispatcher
        )
    }
}