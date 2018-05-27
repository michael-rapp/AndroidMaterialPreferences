# AndroidMaterialPreference - RELEASE NOTES

## Version 4.0.2 (May 27th 2018)

A bugfix release, which fixes the following issues:

- The title, message and content of a `DialogPreference`'s dialog are now scrollable by default, if not enough space is available.
- Fixed wrong default style being used by individual preferences.
- Fixed visual glitches regarding the preview of the `ColorPickerPreference`.

## Version 4.0.1 (May 25th 2018)

A minor release, which introduces the following changes:

- Updated AppCompat v7 support library to version 27.1.1.
- Updated v14 Preference support library to version 27.1.1.
- Updated the dependency "AndroidMaterialDialog" to version 4.3.4.
- Updated the dependency "AndroidMaterialValidation" to version 2.1.6.
- Updated the dependency "AndroidUtil" to version 1.20.3.

## Version 4.0.0 (Apr. 29th 2018)

A major release, which provides the following changes:

- Migrated the library to use the v14 Preference support library as the Android SDK's default preference classes will be deprecated in Android P.
- Updated the dependency "AndroidMaterialDialog" to version 4.3.3.

## Version 3.2.0 (Feb. 9th 2018)

A feature release, which introduces the following changes:

- Added the class `Preference`, which supports to use vector drawables and tinting them even on pre-Lollipop devices.
- (Tinted) Support vector drawables can now be used for the (header) icon of a `DialogPreference`'s dialog even on pre-Lollipop devices.
- Updated dependency "AndroidMaterialDialog" to version 4.3.1.
- Updated dependency "AndroidUtil" to version 1.20.1.

## Version 3.1.3 (Jan. 26th 2018)

A minor release, which introduces the following changes:

- Updated `targetSdkVersion` to API level 27 (Android 8.1).
- Updated dependency "AndroidUtil" to version 1.19.0.
- The data structure `ListenerList` is now used for managing event listeners.

## Version 3.1.2 (Jan. 15th 2018)

A minor release, which introduces the following changes:

- All of the library's preferences now extend the class `AbstractPreference`. It provides a `performClick`-method.

## Version 3.1.1 (Jan. 5th 2018)

A minor release, which introduces the following changes:

- Updated dependency "AndroidMaterialValidation" to version 2.1.4. This fixes crashes on older devices when using an `EditTextPreference`.

## Version 3.1.0 (Dec. 29th 2017)

A feature release, which introduces the following changes:
 
- Added a setter method and theme attributes for customizing the padding of a preference's dialog.
- Added a setter method and a theme attribute for customizing the left and right margin of the divider, which is located above the buttons of a preference's dialog.
- Added setter methods and a theme attribute for setting the window background of a preference's dialog. This allows customize the dialog's shape.
- Added setter methods and theme attributes for specifying the scrollable area of a preference's dialog.
- It is now possible to use custom views for displaying the header of a preference's dialog.
- Fixed an issue, which prevented the switch of a `SwitchPreference` from being adapted when clicking the preference's background.
- Revised the layout of an `ActionPreference`. It now uses an `ImageView` to display the preference's icon and can therefore be used together with arbitrary preferences.
- Updated `targetSdkVersion` to API level 26 (Android 8.0).
- Updated AppCompat v7 support library to version 27.0.2.
- Updated dependency "AndroidUtil" to version 1.18.3. The library now uses the class `AbstractDataBinder` of this dependency instead of shipping its own implementation.
- Updated dependency "AndroidMaterialDialog" to version 4.2.1. In particular, this solves an issue, which caused the background of dialogs to be black on pre-Lollipop devices.

## Version 3.0.1 (Jan. 26th 2017)

A minor release, which introduces the following changes:

- Updated `targetSdkVersion` to API level 25 (Android 7.1).
- Updated AppCompat v7 support library to version 25.1.0.
- Updated dependency "AndroidUtil" to version 1.12.3.
- Updated dependency "AndroidMaterialDialog" to version 4.0.1. This fixes a crash on tablets in portrait mode.
- Updated dependency "AndroidMaterialValidation" to version 2.0.5.

## Version 3.0.0 (Oct. 17th 2016)

A major release, which introduces the following features:

- It is now possible to use custom views for displaying the title, message or buttons of a preference's dialog.
- The library now uses version 4.0.0 of the dependency "AndroidMaterialDialog".
- This enables to use show or hide the dialogs of preferences by using animations.
- It also enables to specify the size and location of a preference's dialog.
- It allows to show the dialog of preferences fullscreen. 

## Version 2.7.1 (Oct. 2nd 2016)

A bugfix release, which fixes the following issues:

- Updated dependency "AndroidMaterialDialog" to version 3.6.10. This fixes a problem with themes not being applied.

## Version 2.7.0 (Sep. 19th 2016)

A feature releae, which introduces the following features:

- Added the preference `ActionPreference`, which acts as a button and displays a centered title.
- It is now possible to set the hint of an `EditTextPreference`'s `EditText` widget.
- It is now possible to set the hints of an `ResolutionPreference`'s `EditText` widgets.

## Version 2.6.4 (Sep. 16th 2016)

A minor release, which introduces the following changes:

- Updated dependency "AndroidMaterialDialog" to version 3.6.8.

## Version 2.6.3 (Sep. 14th 2016)

A minor release, which introduces the following changes:

- Updated dependency "AndroidMaterialDialog" to version 3.6.7.

## Version 2.6.2 (Sep. 13th 2016)

A minor release, which introduces the following changes:

- Updated dependency "AndroidMaterialDialog" to version 3.6.6.

## Version 2.6.1 (Sep. 12th 2016)

A minor release, which introduces the following changes:

- Updated `targetSdkVersion` to API level 24 (Android 7.0).
- Updated AppCompat v7 support library to version 24.2.0.
- Updated dependency "AndroidUtil" to version 1.11.1.
- Updated dependency "AndroidMaterialDialog" to version 3.6.4.
- Updated dependency "AndroidMaterialValidation" to version 2.0.4.

## Version 2.6.0 (Sep. 8th 2016)

A feature release, which introduces the following features:

- The class `AbstractDialogPreference` has been renamed to `DialogPreference` and is not abstract anymore. It can be used to show simple dialogs (optionally) containing a header, title, message and buttons.
- A `setOnClickListener`-method, which allows to register a listener, which is notified, when a button of a preference's dialog is clicked, has been added to the class `DialogPreference`.
- A `setOnShowListener`-method, which allows to register a listener, which is notified, when a preference's dialog is shown, has been added to the class `DialogPreference`.
- A `setOnDismissListener`-method, which allows to register a listener, which is notified, when a preference's dialog is dismissed, has been added to the class `DialogPreference`.
- A `setOnCancelListener`-method, which allows to register a listener, which is notified, when a preference's dialog is canceled, has been added to the class `DialogPreference`.
- A `setOnKeyListener`-method, which allows to register a listener, which is notified, when a key is dispatched to a preference's dialog, has been added to the class `DialogPreference`.

## Version 2.5.2 (Sep. 7th 2016)

A bugfix release, which introduces the following changes:

- Updated dependency "AndroidMaterialValidation" to version 2.0.3. This fixes a crash when showing an `EditTextPreference`'s dialog on devices with API level less than 16. 

## Version 2.5.1 (Aug. 19th 2016)

A minor release, which introduces the following changes:

- Updated dependency "AndroidMaterialDialog" to version 3.6.3.

## Version 2.5.0 (Aug. 19th 2016)

A feature release, which introduces the following changes:

- The `showDialogDividersOnScroll`-method is now available for all preferences, which show a dialog.
- Replaced `setWidth`- and `setHeight`-methods of the `ResolutionPreference` with a `setResolution`-method, which allows to set both values at once. This prevents inconsistencies where only one of both values is set.
- Updated dependency "AndroidMaterialDialog" to version 3.6.0.
- Updated dependency "AndroidUtil" to version 1.11.0.

## Version 2.4.2 (Jul. 10th 2016)

A bugfix release, which fixes the following issues:

- https://github.com/michael-rapp/AndroidMaterialPreferences/issues/13, respectively https://github.com/michael-rapp/AndroidMaterialPreferences/issues/2
- Updated dependency "AndroidUtil" to version 1.8.1.

## Version 2.4.1 (Jun. 16th 2016)

A bugfix release, which introduces the following changes:

- The `OnPreferenceChangeListener` of a `SwitchPreference` is now invoked, when the preference`s switch is toggled.

## Version 2.4.0 (May 26th 2016)

A feature release, which introduces the following changes:

- Updated dependency "AndroidMaterialDialog" to version 3.5.0. Therefore the possibility to specify, whether the divider, which is located above the custom view of a preference's dialog, should be shown, or not, is not available anymore.
- Added the possibility to set, whether the dividers, which are located above and below the list view of a `ListPreference`'s or `MultiChoiceListPreference`'s dialog, should be shown when the list view is scrolled, or not.

## Version 2.3.1 (May 25th 2016)

A minor release, which introduces the following changes:

- Updated dependency "AndroidMaterialDialog" to version 3.4.2.
- When no theme is specified using the theme attribute `preferenceDialogTheme` or the XML attribute `custom:dialogThemeResource`, the light theme is now used instead of the dark one for the preference's dialogs.

## Version 2.3.0 (May 25th 2016)

A feature release, which introduces the following changes:

- Updated dependency "AndroidMaterialDialog" to version 3.4.1. This introduces the dialogs of preferences to appear slightly different.
- Theming the dialogs of preferences is now properly supported by either globally specifying a theme using the theme attribute `preferenceDialogTheme`, or by specifying a theme using the XML attribute `custom:dialogThemeResource` on a per-preference basis.
- It is now possible to show a divider above the button bar and/or custom view of a preference's dialog.
- The possibility to specify the control color of a `ListPreference` or `MultiChoiceListPreference` has been removed as it caused problems.

## Version 2.2.0 (May 23th 2016)

A feature release, which introduces the following changes:

- Updated dependency "AndroidMaterialDialog" to version 3.3.0. This enables the dialog of preferences to contain a header.
- Updated AppCompat v7 support library to version 23.4.0.

## Version 2.1.0 (May 2nd 2016)

A feature release, which introduces the following changes:

- It is now possible to select negative numbers using a `NumberPickerPreference` (https://github.com/michael-rapp/AndroidMaterialPreferences/issues/12) or a `SeekBarPreference`.
- Updated dependency "AndroidUtil" to version 1.4.11.

## Version 2.0.9 (Mar. 21th 2016)

A bugfix release, which introduces the following changes:

- Updated dependency "AndroidUtil" to version 1.4.7, because the previous version conflicted with the Android design library.

## Version 2.0.8 (Mar. 21th 2016)

A bugfix release, which introduces the following changes:

- Fixed faulty positions and sizes of `DialogPreferences`' widgets introduced by the previous release.
- Updated dependency "AndroidUtil" to version 1.4.6.

## Version 2.0.7 (Mar. 18th 2016)

A minor release, which introduces the following changes:

- Updated dependency "AndroidMaterialDialog" to version 3.2.3.

## Version 2.0.6 (Mar. 17th 2016)

A minor release, which introduces the following changes:

- Updated dependency "AndroidUtil to version 1.4.5.
- Updated dependency "AndroidMaterialDialog" to version 3.2.2.
- Updated dependency "AndroidMaterialValidation" to version 3.0.2.

## Version 2.0.5 (Mar. 15th 2016)

A minor release, which introduces the following changes:

- Updated dependency "AndroidUtil" to version 1.4.4.
- Updated dependency "AndroidMaterialDialog" to version 3.2.1 (this increases the width of a preference's dialog on smartphones).
- Updated AppCompat support library to version 23.2.1 (this prevents the example app from crashing because of a bug in the previous version. See here for details: https://code.google.com/p/android/issues/detail?id=201817).

## Version 2.0.4 (Feb. 25th 2016)

A minor release, which introduces the following changes:

- The library is from now on distributed under the Apache License version 2.0. 
- Updated dependency "AndroidUtil" to version 1.4.3.
- Updated dependency "AndroidMaterialDialog" to version 3.1.1.
- Updated dependency "AndroidMaterialValidation" to version 2.0.1.
- Updated AppCompat support library to version 23.2.0.
- Minor changes of the example app.

## Version 2.0.3 (Feb. 13th 2016)

A minor release, which introduces the following changes:

- Version 3.1.0 of the library "AndroidMaterialDialog" is now used.

## Version 2.0.2 (Dec. 13th 2015)

A minor release, which introduces the following changes:

- Implemented enhancement https://github.com/michael-rapp/AndroidMaterialPreferences/issues/6 by updating to version 3.0.1 of the library "AndroidMaterialDialog".

## Version 2.0.1 (Nov. 12th 2015)

A minor release, which introduces the following changes:

- Version 3.0.0 of the library "AndroidMaterialDialog" is now used.

## Version 2.0.0 (Oct. 19th 2015)

A major release, which introduces the following changes:

- The project has been migrated from the legacy Eclipse ADT folder structure to Android Studio. It now uses the Gradle build system and the library as well as the example app are contained by one single project.
- The library can now be added to Android apps using the Gradle dependency `com.github.michael-rapp:android-material-preferences:2.0.0`

## Version 1.7.1 (Sept. 24th 2015)

A minor release, which introduces the following changes:

- The number of columns and preview size, which is used to show colors in the dialog of a `ColorPalettePreference` has been adapted on tablets.

## Version 1.7.0 (Sept. 16th 2015)

A feature release, which introduces the following new features:

- Added a preference, which allows to select multiple values from a list (`MultiChoiceListPreference`).
- Previews of the `ColorPalettePreference` are now created asynchronously.
- Round previews of the `ColorPalettePreference` are now rendered antialiased when using no border.

# Version 1.6.0 (Sept. 13th 2015)

A feature release, which introduces the following changes:

- The library does now rely on version 1.2.0 of the library "AndroidMaterialDialog".
- Additional setter/getter methods and XML attributes have been added in order to support greater customization of preferences' dialogs. All dialog preferences do now allow to specify the message color and background of their dialogs and the `ListPreference` furthermore allows to specify the color of the list items and controls.

## Version 1.5.0 (Sept. 11th 2015)

A feature release, which introduces the following new features:

- The preferences `NumberPickerPreference` and `DigitPickerPreference` do now allow to set an unit, which is used for printing textual representations of the preferences' current numbers.
- The preference `NumberPickerPreference` does now support to set the step size, the preference's number should be increased or decreased by when moving its selector wheel.
- Some attributes have been renamed for greater consistency between the library's preferences.
- Fixed issue https://github.com/michael-rapp/AndroidMaterialPreferences/issues/3

## Version 1.4.0 (Aug. 31th 2015)

A feature release, which introduces the following new features:

- Added a preference, which allows to choose a color from a predefined color palette (`ResolutionPreference`).
- Added a preference, which provides a toggleable option using a `Switch` widget (`SwitchPreference`).
- Minor implementation and API changes for greater consistency with the Android SDK's built-in preferences.

## Version 1.3.0 (Aug. 22th 2015)

A feature release, which introduces the following new features:

- Added a preference, which allows to enter two-dimensional image or video resolutions using two `EditText` widgets (`ResolutionPreference`).

## Version 1.2.0 (June 8th 2015)

A feature release, which introduces the following new features:

- An `EditTextPreference` can now be validated by using the library "AndroidMaterialValidation".

## Version 1.1.1 (Apr. 3rd 2015)

A bugfix release, which fixes the following issues:

- https://github.com/michael-rapp/AndroidMaterialPreferences/issues/1

## Version 1.1.0 (Apr. 3rd 2015)

A feature release, which introduces the following new features:

- Validators can now be added to an `EditTextPreference` in order to validate the entered text and show error messages if needed.
- Added a preference, which allows to choose a decimal number via a `NumberPicker` widget (`NumberPickerPreference`).
- Added a preference, which allows to choose a decimal number using multiple `NumberPicker` widgets, which allow to choose each digit individually (`DigitPickerPreference`).

## Version 1.0.0 (Nov. 19th 2014)

The first stable release, which initially provides the following preferences, which are designed according to Android 5's Material Design guidelines even on pre-Lollipop devices:
	
- A preference, which allows to enter a text via an `EditText` widget (`EditTextPreference`).
- A preference, which allows to select a value from a list (`ListPreference`).
- A preference, which allows to choose a floating point value or an integer value from a continuous range via a `SeekBar` widget (`SeekBarPreference`).