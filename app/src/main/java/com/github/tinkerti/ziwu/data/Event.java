package com.github.tinkerti.ziwu.data;

import com.github.tinkerti.ziwu.data.model.TaskRecordInfo;

public class Event {

    public static class RecordEvent {
        public TaskRecordInfo recordInfo;

        public RecordEvent(TaskRecordInfo recordInfo) {
            this.recordInfo = recordInfo;
        }
    }
}
