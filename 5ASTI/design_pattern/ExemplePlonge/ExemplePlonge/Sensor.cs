using System;
using System.Collections.Generic;
using System.Text;

namespace ExemplePlonge
{
    class Sensor
    {
        public double Pressure { get; private set; }

        public async double ReadAsync()
        {
            // ... Logique
            return 1.0d;
        }
    }
}
