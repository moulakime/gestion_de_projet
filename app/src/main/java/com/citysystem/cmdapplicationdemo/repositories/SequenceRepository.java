package com.citysystem.cmdapplicationdemo.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.citysystem.cmdapplicationdemo.db.daos.SequenceDao;
import com.citysystem.cmdapplicationdemo.db.room.CmdDataBase;
import com.citysystem.cmdapplicationdemo.entities.Sequence;

public class SequenceRepository {
    private SequenceDao sequenceDao;

    public SequenceRepository(Application application) {
        CmdDataBase database = CmdDataBase.getInstance(application);
        sequenceDao = database.sequenceDao();
    }

    public void updateCounterSequence(String type) {
        Sequence sequence = sequenceDao.getSequence(type);
        long counter = sequence.getCounter() + 1;
        new UpdateCounterSequenceAsyncTask(sequenceDao).execute(counter, type);
    }

    private static class UpdateCounterSequenceAsyncTask extends AsyncTask<Object, Void, Void> {
        private SequenceDao sequenceDao;

        public UpdateCounterSequenceAsyncTask(SequenceDao sequenceDao) {
            this.sequenceDao = sequenceDao;
        }

        @Override
        protected Void doInBackground(Object... params) {
            long counter = (long) params[0];
            String type = (String) params[1];
            sequenceDao.updateCountSequence(counter, type);
            return null;
        }
    }

}
