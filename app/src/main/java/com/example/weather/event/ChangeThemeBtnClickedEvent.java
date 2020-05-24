package com.example.weather.event;

public class ChangeThemeBtnClickedEvent {
    private boolean checked;

    public ChangeThemeBtnClickedEvent(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }
}
