using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ExemplePlonge
{
    class PressureLoggerService
    {
        private readonly LogCollection logCollection;

        public PressureLoggerService(LogCollection logCollection)
        {
            this.logCollection = logCollection;
        }

        public void WriteLog(double pressure)
        {
            if (this.logCollection.Logs.Count != 0)
            {
                double lastPressure = this.logCollection.Logs.Last().Pressure;
                if (lastPressure != pressure)
                {
                    var newLog = new Log(pressure);
                    this.logCollection.Logs.Add(newLog);
                }
            }
            else
            {
                var newLog = new Log(pressure);
                this.logCollection.Logs.Add(newLog);
            }
        }

        public Log ReverseFindLog(int index)
        {
            int length = this.logCollection.Logs.Count;
            if(length < index)
            {
                index = 0;
            }
            return this.logCollection.Logs[length - index];
        }
    }
}
