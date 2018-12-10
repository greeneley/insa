using System;
using System.Collections.Generic;
using System.Text;

namespace ExemplePlonge
{
    class PressureStrategist : IPressureObserver
    {
        private IPressureStrategy CurrentStrategy;

        public bool Execute(double pressure)
        {
            throw new NotImplementedException();
        }
    }
}
