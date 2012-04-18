# This file finds libpcap, if present on the system, and defines the following variables:
# 

IF(WIN32)
#Currently, we don't have anything written for using WinPcap, so...
ELSE(WIN32)
	FIND_PATH( PCAP_INCLUDE_DIRS
		pcap.h
		/usr/include
		/usr/local/include
		/opt/local/include
		DOC "Directory in which pcap.h resides"
		)
	FIND_LIBRARY( PCAP_LIBRARIES
		NAMES pcap
		PATHS
		/usr/lib64
		/usr/lib
		/usr/local/lib64
		/usr/local/lib
		/opt/local/lib
		DOC "Pcap library for packet capture"
		)
ENDIF(WIN32)


IF(PCAP_INCLUDE_DIRS)
	SET(PCAP_FOUND 1 CACHE STRING "Set to 1 if libpcap is found, 0 otherwise")
ELSE(PCAP_INCLUDE_DIRS)
	SET(PCAP_FOUND 0 CACHE STRING "Set to 1 if libpcap is found, 0 otherwise")
ENDIF(PCAP_INCLUDE_DIRS)

MARK_AS_ADVANCED(PCAP_FOUND)
