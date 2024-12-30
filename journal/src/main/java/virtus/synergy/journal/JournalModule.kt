package virtus.synergy.journal

import androidx.room.Room
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import virtus.synergy.journal.model.db.MindTempusDataBase
import virtus.synergy.journal.screens.journal.details.JournalDetailsViewModel
import virtus.synergy.journal.screens.journal.list.JournalListViewModel
import virtus.synergy.journal.screens.journal.selector.EmotionsSelectorViewModel
import virtus.synergy.journal.screens.settings.SettingViewModel
import virtus.synergy.journal.usecases.JournalUseCase
import virtus.synergy.journal.usecases.JournalUseCaseImpl
import virtus.synergy.journal.usecases.SettingUseCase
import virtus.synergy.journal.usecases.SettingUseCaseImpl

/**
 * MindTempus
 * Created on 02/07/2023.
 * Author: johan
 */

val journalModule = module {

    single {
        Room.databaseBuilder(
            get(),
            MindTempusDataBase::class.java,
            "mindTempus.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    factory<JournalUseCase> {
        JournalUseCaseImpl(get())
    }
    factory<SettingUseCase> {
        SettingUseCaseImpl(get())
    }
    viewModelOf(::JournalDetailsViewModel)
    viewModelOf(::JournalListViewModel)
    viewModelOf(::SettingViewModel)
    viewModelOf(::EmotionsSelectorViewModel)
}