package com.theikchan.screencloudwebplaybridge.data.repo

import android.util.Log
import com.theikchan.screencloudwebplaybridge.NativeStore
import com.theikchan.screencloudwebplaybridge.domain.model.DeviceDetailInfo
import com.theikchan.screencloudwebplaybridge.domain.repo.DeviceInfoRepository
import com.theikchan.screencloudwebplaybridge.utils.JsonParser
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DeviceInfoRepositoryImpl @Inject constructor(
    private val nativeStore: NativeStore,
    private val androidDeviceInfoRepoImpl: AndroidDeviceInfoRepoImpl
) : DeviceInfoRepository {

    override suspend fun getAndroidDeviceInfo(): String {

        val appVersion = androidDeviceInfoRepoImpl.getAppVersion()
        val packageName = androidDeviceInfoRepoImpl.getPackageName()
        val screenWidth = androidDeviceInfoRepoImpl.getScreenWidth().toInt()
        val screenHeight = androidDeviceInfoRepoImpl.getScreenHeight().toInt()
        val screenDensity = androidDeviceInfoRepoImpl.getScreenDensity().toInt()
        val androidVersion = androidDeviceInfoRepoImpl.getAndroidVersion()
        val deviceManufacturer = androidDeviceInfoRepoImpl.getManufacturer()
        val deviceModel = androidDeviceInfoRepoImpl.getModel()

        return """{
            "app_version": "$appVersion",
            "package_name": "$packageName",
            "screen_width": $screenWidth,
            "screen_height": $screenHeight,
            "screen_density": $screenDensity,
            "android_version": $androidVersion,
            "device_manufacturer": "$deviceManufacturer",
            "device_model": "$deviceModel"
        }"""
    }

    override suspend fun getNativeDeviceInfo(): String =
        nativeStore.getDeviceInfo()

    override suspend fun getDeviceDetailInfo(): DeviceDetailInfo {
        val nativeDeviceInfo = getNativeDeviceInfo()
        val androidDeviceInfo = getAndroidDeviceInfo()
        val deviceDetailInfoString = JsonParser.mergeJson(androidDeviceInfo, nativeDeviceInfo)
        return Json.decodeFromString(deviceDetailInfoString)
    }
}