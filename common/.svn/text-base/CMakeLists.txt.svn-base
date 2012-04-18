
INCLUDE_DIRECTORIES( ${INAV_SOURCE_DIR}/common )

ADD_DEFINITIONS("-Wall")

IF(CMAKE_BUILD_TYPE STREQUAL "debug")
	ADD_DEFINITIONS("-g3 -ggdb ")
ENDIF(CMAKE_BUILD_TYPE STREQUAL "debug")

IF(CMAKE_BUILD_TYPE STREQUAL "release")
	ADD_DEFINITIONS("-O2 -funroll-loops -fprefetch-loop-arrays ")
ENDIF(CMAKE_BUILD_TYPE STREQUAL "release")


ADD_LIBRARY(inavserver 
	commandLineParser.cpp
	helper.cpp
	mutex.cpp
	parseCommas.cpp
	semaphore.cpp
	threads.cpp
	xmlParser.cpp
	)

