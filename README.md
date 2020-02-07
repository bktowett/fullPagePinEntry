# fullPagePinEntry
This is a pin entry library that provides a full page view. Based on https://github.com/aritraroy/PinLockView library
Sample usage

`PinEntryFragment.Builder(this)
.setResultListener {
            showToast(it)
        }
.setLabel("Enter transaction PIN")
.setIcon(R.drawable.ic_calendar_icon)
.setPinLength(6)
.build()`
