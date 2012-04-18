OBJS= trafficGenerator.o
CC=g++
CXXFLAGS=  -g
LDFLAGS= -lpthread -lpcap
LDTESTFLAGS= $(LDFLAGS) -ldl -lcppunit
DEPEND= makedepend $(CFLAGS)

trafficGen: $(OBJS)
	$(CC) $(LDFLAGS) -o $@ $(OBJS)


clean:
	-rm *.o trafficGen 
