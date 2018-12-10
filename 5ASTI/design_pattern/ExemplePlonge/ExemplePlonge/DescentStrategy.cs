using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ExemplePlonge
{
    class DescentStrategy : IPressureStrategy
    {
        protected readonly PressureLoggerService pressureLoggerService;

        public DescentStrategy(PressureLoggerService pressureLoggerService)
        {
            this.pressureLoggerService = pressureLoggerService;
        }

        public void Apply(double pressure)
        {
            pressureLoggerService.WriteLog(pressure);
        }
    }
}
