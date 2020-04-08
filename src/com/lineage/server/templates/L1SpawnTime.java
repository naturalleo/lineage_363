package com.lineage.server.templates;

import java.sql.Time;
import java.sql.Timestamp;

import com.lineage.server.utils.TimePeriod;

public class L1SpawnTime {

    private final int _spawnId;
    private final Time _timeStart;
    private final Time _timeEnd;
    private final TimePeriod _timePeriod;
    private final Timestamp _periodStart;
    private final Timestamp _periodEnd;
    private boolean _isDeleteAtEndTime;

    public boolean isDeleteAtEndTime() {
        return _isDeleteAtEndTime;
    }

    private L1SpawnTime(final L1SpawnTimeBuilder builder) {
        _spawnId = builder._spawnId;
        _timeStart = builder._timeStart;
        _timeEnd = builder._timeEnd;
        _timePeriod = new TimePeriod(this._timeStart, this._timeEnd);
        _periodStart = builder._periodStart;
        _periodEnd = builder._periodEnd;
        _isDeleteAtEndTime = builder._isDeleteAtEndTime;
    }

    public int getSpawnId() {
        return _spawnId;
    }

    public Time getTimeStart() {
        return _timeStart;
    }

    public Time getTimeEnd() {
        return _timeEnd;
    }

    public Timestamp getPeriodStart() {
        return _periodStart;
    }

    public Timestamp getPeriodEnd() {
        return _periodEnd;
    }

    public static class L1SpawnTimeBuilder {
        private final int _spawnId;
        private Time _timeStart;
        private Time _timeEnd;
        private Timestamp _periodStart;
        private Timestamp _periodEnd;
        private boolean _isDeleteAtEndTime;

        public L1SpawnTimeBuilder(final int spawnId) {
            _spawnId = spawnId;
        }

        public L1SpawnTime build() {
            return new L1SpawnTime(this);
        }

        public void setTimeStart(final Time timeStart) {
            _timeStart = timeStart;
        }

        public void setTimeEnd(final Time timeEnd) {
            _timeEnd = timeEnd;
        }

        public void setPeriodStart(final Timestamp periodStart) {
            _periodStart = periodStart;
        }

        public void setPeriodEnd(final Timestamp periodEnd) {
            _periodEnd = periodEnd;
        }

        public void setDeleteAtEndTime(final boolean f) {
            _isDeleteAtEndTime = f;
        }

    }

    public TimePeriod getTimePeriod() {
        return _timePeriod;
    }
}
