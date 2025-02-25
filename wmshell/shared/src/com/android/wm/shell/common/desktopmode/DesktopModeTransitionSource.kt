package com.android.wm.shell.common.desktopmode

import android.os.Parcel
import android.os.Parcelable

/** Transition source types for Desktop Mode. */
enum class DesktopModeTransitionSource : Parcelable {
    /** Transitions that originated as a consequence of task dragging. */
    TASK_DRAG,
    /** Transitions that originated from an app from Overview. */
    APP_FROM_OVERVIEW,
    /** Transitions that originated from app handle menu button */
    APP_HANDLE_MENU_BUTTON,
    /** Transitions that originated as a result of keyboard shortcuts. */
    KEYBOARD_SHORTCUT,
    /** Transitions with source unknown. */
    UNKNOWN;

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
    }

    companion object {
        @JvmField
        val CREATOR =
            object : Parcelable.Creator<DesktopModeTransitionSource> {
                override fun createFromParcel(parcel: Parcel): DesktopModeTransitionSource {
                    return parcel.readString()?.let { valueOf(it) } ?: UNKNOWN
                }

                override fun newArray(size: Int) = arrayOfNulls<DesktopModeTransitionSource>(size)
            }
    }
}
