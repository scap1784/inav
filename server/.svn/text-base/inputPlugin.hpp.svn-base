#ifndef INPUTPLUGIN_HPP
#define INPUTPLUGIN_HPP

#include <string>
#include "dataClassifications.hpp"
#include "graph.hpp"

class InputPlugin {
  protected:
    DataQueue dataQueue;

  public:
    //constructor
    InputPlugin()
    {}

    //deconstructor
    virtual ~InputPlugin() {}

    //Returns the types of data that the plugin can gather
    virtual DataClassifications typesOfDataToReport() {}

    //Gets the xml config to send to the client
    virtual std::string getConfig() {}

    //Users changes to the config in XML
    virtual std::string setConfig( std::string ) {}

    //Thread to gather data 
    virtual void* inputThread( void *data ) {}


};

typedef InputPlugin* create_t();
typedef void destroy_t( InputPlugin* );

#endif
