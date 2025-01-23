package com.theikchan.screencloudwebplaybridge

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.theikchan.screencloudwebplaybridge.domain.repo.DeviceInfoRepository
import com.theikchan.screencloudwebplaybridge.domain.usecase.GetDeviceInfoUseCase
import com.theikchan.screencloudwebplaybridge.navigation.DeviceDetailInfoPara
import com.theikchan.screencloudwebplaybridge.repo.FakeDeviceInfoRepository
import com.theikchan.screencloudwebplaybridge.ui.deviceinfodialog.DeviceInfoDialog
import com.theikchan.screencloudwebplaybridge.ui.deviceinfodialog.DeviceInfoDialogViewModel
import com.theikchan.screencloudwebplaybridge.ui.main.ScreenCloudWebPlay
import com.theikchan.screencloudwebplaybridge.ui.main.ScreenCloudWebPlayViewModel
import com.theikchan.screencloudwebplaybridge.ui.theme.ScreenCloudWebPlayBridgeTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Inject
    lateinit var mockDeviceInfoRepository: FakeDeviceInfoRepository

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testWebViewLoading() {
        // Test - WebView loading verification
        // Launch the app
        // Check WebView exist
        // check WebView loading finished
        val mockUrl = "file:///android_asset/index.html"
        val mockGetDeviceInfoUseCase = GetDeviceInfoUseCase(mockDeviceInfoRepository)
        val mockViewModel = ScreenCloudWebPlayViewModel(
            getDeviceInfoUseCase = mockGetDeviceInfoUseCase,
        )

        composeTestRule.setContent {
            ScreenCloudWebPlay(
                viewModel = mockViewModel,
                onDeviceInfoPressed = {},
                onScreenshotPressed = {})
        }

        val webViewTag = "webView"
        composeTestRule.onNodeWithTag(webViewTag).assertExists()

        assertTrue(mockViewModel.isWebViewLoaded.value == true)
        assertTrue(mockViewModel.webViewLoadedUrl.value == mockUrl)

        composeTestRule.onNodeWithTag(webViewTag).assertExists()
    }

    @Test
    fun testScreenshot() {
        // Test - Screenshot verification
        // Launch the app
        // Check screenshot button exist
        // Perform click on screenshot button
        val mockGetDeviceInfoUseCase = GetDeviceInfoUseCase(mockDeviceInfoRepository)
        val mockViewModel = ScreenCloudWebPlayViewModel(
            getDeviceInfoUseCase = mockGetDeviceInfoUseCase,
        )

        composeTestRule.setContent {
            ScreenCloudWebPlay(viewModel = mockViewModel, onDeviceInfoPressed = {},
                onScreenshotPressed = {
                    assertTrue(it)
                })
        }

        composeTestRule.onNodeWithTag("webView").assertExists()

        composeTestRule.onNodeWithTag("screenshotButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("screenshotButton").performClick()
    }

    @Test
    fun testDeviceInfo() {
        // Test - Device info accuracy
        // Check device info dialog display content correct

        // Arrange: Mock the data for testing
        val mockDeviceInfo = DeviceDetailInfoPara(
            appVersion = "1.0",
            packageName = "io.screencloud.assignment.android_senior_dev",
            screenWidth = 2560,
            screenHeight = 1688,
            screenDensity = 2.0,
            deviceManufacturer = "Genymobile",
            deviceModel = "Pixel C",
            cpuCores = "3",
            cpuFreq = "3072.000",
            ramTotal = "3148120064",
            kernelVersion = "11",
            buildFingerprint = "google/vbox86p/vbox86p:11/RQ1A.210105.003/857:userdebug/test-keys,p1",
            hardwareSerial = "vbox86",
            androidVersion = 30,
        )
        val mockDeviceInfoDialogViewModel = DeviceInfoDialogViewModel(mockDeviceInfo)

        // Act: Render the dialog with mocked data
        composeTestRule.setContent {
            ScreenCloudWebPlayBridgeTheme {
                DeviceInfoDialog(
                    viewModel = mockDeviceInfoDialogViewModel,
                    onClosePress = {}
                )
            }
        }

        // Assert: Verify that the correct information is displayed
        composeTestRule.onNodeWithText("Device Information").assertIsDisplayed()

        composeTestRule.onNodeWithText("App Info:").assertIsDisplayed()
        composeTestRule.onNodeWithText("Version: 1.0\nPackage: io.screencloud.assignment.android_senior_dev")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Screen Info:").assertIsDisplayed()
        composeTestRule.onNodeWithText("Width: 2560px\nHeight: 1688px\nDensity: 2.0")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Device Info:").assertIsDisplayed()
        composeTestRule.onNodeWithText("Manufacturer: Genymobile\nModel: Pixel C\nAndroid Version: 30")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("CPU Info:").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cores: 3\nFrequency: 3072.000 MHz").assertIsDisplayed()

        composeTestRule.onNodeWithText("System Info:").assertIsDisplayed()
        composeTestRule.onNodeWithText("RAM Total: 2.93 GB\nKernel Version: 11").assertIsDisplayed()

        composeTestRule.onNodeWithTag("deviceInfoDialogCloseButton").assertExists()
    }
}