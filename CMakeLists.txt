cmake_minimum_required(VERSION 2.6)
project(INAV)

IF(NOT CMAKE_BUILD_TYPE)
	SET(CMAKE_BUILD_TYPE "debug" CACHE STRING "Build type determining compiler flags" )
ENDIF(NOT CMAKE_BUILD_TYPE)

add_subdirectory(common)
add_subdirectory(server)
# Still under development, don't look for it by default
#add_subdirectory(inavQT)
