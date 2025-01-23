//
// Created by Theik Chan on 24/01/2025.
//

#ifndef SCREENCLOUDWEBPLAYBRIDGE_SYSTEMINFO_H
#define SCREENCLOUDWEBPLAYBRIDGE_SYSTEMINFO_H

#include <string>

class SystemInfo {
public:
    std::string getRamTotal();
    std::string getCpuInfo();
    std::string getKernelVersion();
    std::string getBuildFingerprint();
    std::string getHardwareSerial();
    std::string getDeviceInfoJson();
};

#endif //SCREENCLOUDWEBPLAYBRIDGE_SYSTEMINFO_H
