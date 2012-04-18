
SET(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} ${CMAKE_CURRENT_SOURCE_DIR}/cmake/ )

FIND_PACKAGE(Threads REQUIRED)
FIND_PACKAGE(Pcap REQUIRED)
# Still need some FindPcap.cmake file
#MESSAGE(STATUS "Looking for FindPcap.cmake in " ${CMAKE_MODULE_PATH})
#INCLUDE(FindPcap.cmake)

IF(PCAP_FOUND)
	MESSAGE(STATUS "Found libpcap")
ELSE(PCAP_FOUND)
	MESSAGE(FATAL_ERROR "Couldn't find libpcap")
ENDIF(PCAP_FOUND)

IF(NOT CMAKE_BUILD_TYPE)
	SET(CMAKE_BUILD_TYPE "debug")
ENDIF(NOT CMAKE_BUILD_TYPE)

ADD_DEFINITIONS("-Wall")

# Check to see if platform is BSD-based - one of the system headers is different for ethernet octets
IF(${CMAKE_SYSTEM_NAME} MATCHES ".*BSD.*" OR ${CMAKE_SYSTEM_NAME} MATCHES "Darwin" )
       ADD_DEFINITIONS("-DBSD ")
ENDIF(${CMAKE_SYSTEM_NAME} MATCHES ".*BSD.*" OR ${CMAKE_SYSTEM_NAME} MATCHES "Darwin" )

# Hack to get stuff to build on OSX - see http://public.kitware.com/Bug/view.php?id=6195
# and http://lists.apple.com/archives/Xcode-users/2007/Oct/msg00696.html
IF(${CMAKE_SYSTEM_NAME} MATCHES "Darwin")
	ADD_DEFINITIONS("-mmacosx-version-min=10.4 ")
ENDIF(${CMAKE_SYSTEM_NAME} MATCHES "Darwin")


IF(CMAKE_BUILD_TYPE STREQUAL "debug")
	ADD_DEFINITIONS("-g3 -ggdb ")
ENDIF(CMAKE_BUILD_TYPE STREQUAL "debug")

IF(CMAKE_BUILD_TYPE STREQUAL "release")
	ADD_DEFINITIONS("-O2 -funroll-loops -fprefetch-loop-arrays ")
ENDIF(CMAKE_BUILD_TYPE STREQUAL "release")


SET(INAVD_SOURCES
	bandwidthMonitor.cpp
	baseData.cpp
	clientComm.cpp
	clientCommData.cpp
	dataQueue.cpp
	debugThread.cpp
	ethernet.cpp
	filterData.cpp
	graphData.cpp
	icmp.cpp
	inavServer.cpp
	ip.cpp
	packet.cpp
	sniffer.cpp
	snifferData.cpp
	tcp.cpp
	traceroute/tracerouteData.cpp
	traceroute/tracerouteThread.cpp
	udp.cpp
	)


INCLUDE_DIRECTORIES(${INAV_SOURCE_DIR}/common ${INAV_SOURCE_DIR}/server ${PCAP_INCLUDE_PATH})

LINK_DIRECTORIES(${INAV_BINARY_DIR}/common )

ADD_EXECUTABLE(inavd ${INAVD_SOURCES})
TARGET_LINK_LIBRARIES(inavd inavserver ${CMAKE_THREAD_LIBS_INIT} ${PCAP_LIBRARIES})

