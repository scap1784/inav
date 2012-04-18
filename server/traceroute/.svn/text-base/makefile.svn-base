OBJS= traceroute.o tracerouteData.o tracerouteThread.o ../icmp.o ../semaphore.o ../baseData.o ../packet.o ../ethernet.o ../tcp.o ../ip.o ../commandLineParser.o ../parseCommas.o ../udp.o
TESTOBJS=$(OBJS) tester.o 
SERVOBJS=$(OBJS) inavServer.o
CC=g++
CXXFLAGS= -O2 -g
LDFLAGS= -lpthread -lpcap
LDTESTFLAGS= $(LDFLAGS) -ldl -lcppunit
DEPEND= makedepend $(CFLAGS)

#inavd: $(SERVOBJS)
#	$(CC) $(LDFLAGS) -o $@ $(SERVOBJS)

all: traceroute 

traceroute: $(OBJS)

clean:
	-rm $(OBJS) traceroute  
