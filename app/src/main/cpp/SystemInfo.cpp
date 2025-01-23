//
// Created by Theik Chan on 24/01/2025.
//

#include "SystemInfo.h"
#include <sys/sysinfo.h>
#include <fstream>
#include <sstream>
#include <unistd.h>
#include <thread>


std::string SystemInfo::getRamTotal() {
    struct sysinfo info;
    sysinfo(&info);
    std::ostringstream oss;
    oss << info.totalram;
    return oss.str();
}

std::string SystemInfo::getCpuInfo() {
    std::ostringstream oss;
    oss << "{";

    // Get the number of CPU cores
    unsigned int numCores = std::thread::hardware_concurrency();
    oss << "\"cpu_cores\": \"" << numCores << "\",";

    // Get the CPU frequency
    std::ifstream cpuMaxFreq("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
    std::string maxFreq;
    std::getline(cpuMaxFreq, maxFreq);
    oss << "\"cpu_freq\": \"" << maxFreq << "\"";

    oss << "}";

    return oss.str();
}

std::string SystemInfo::getKernelVersion() {
    std::ifstream versionFile("/proc/version");
    std::string kernel_version;
    std::getline(versionFile, kernel_version);
    return kernel_version;
}

std::string SystemInfo::getBuildFingerprint() {
    std::ifstream fingerprint("/system/build.prop");
    std::string line, build_fingerprint;
    while(std::getline(fingerprint, line)) {
        if (line.find("ro.build.fingerprint") != std::string::npos) {
            build_fingerprint = line.substr(line.find("=") + 1);
            break;
        }
    }
    return build_fingerprint;
}

std::string SystemInfo::getHardwareSerial() {
    std::ifstream serial("/proc/serial");
    std::string hardware_serial;
    std::getline(serial, hardware_serial);
    return hardware_serial;
}

std::string SystemInfo::getDeviceInfoJson() {
    std::string json = R"({
        "ram_total": ")" + getRamTotal() + R"(",
        "cpu_info": )" + getCpuInfo() + R"(,
        "kernel_version": ")" + getKernelVersion() + R"(",
        "build_fingerprint": ")" + getBuildFingerprint() + R"(",
        "hardware_serial": ")" + getHardwareSerial() + R"("
    })";
    return json;
}

//std::string SystemInfo::getDeviceInfoJson() {
//    std::string native_info = R"({
//        "ram_total": "3148120064",
//        "cpu_info": {
//            "cpu_cores": "3",
//            "cpu_freq": "3072.000"
//        },
//        "kernel_version": "11",
//        "build_fingerprint": "google/vbox86p/vbox86p:11/RQ1A.210105.003/857:userdebug/test-keys,p1",
//        "hardware_serial": "vbox86"
//    })";
//
//    return native_info;
//}