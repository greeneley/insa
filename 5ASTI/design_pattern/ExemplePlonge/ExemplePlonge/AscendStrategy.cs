using System;
using System.Collections.Generic;
using System.Text;

namespace ExemplePlonge
{
    internal class AscendStrategy : DescentStrategy
    {
        private const double 
            MAX_SPEED = 0.1;

        public AscendStrategy(PressureLoggerService pressureLoggerService) : base(pressureLoggerService)
        {
        }

        public new void Apply(double pressure)
        {
            pressureLoggerService.WriteLog(pressure);
            double speed = ComputeSpeed();

            if(speed > maxSpeed)
            {
                Buzzer.bip(12);
            }
        }

        public double ComputeSpeed()
        {
            Log lastLog   = pressureLoggerService.ReverseFindLog(1);
            Log log       = pressureLoggerService.ReverseFindLog(0);

            var distance  = ((lastLog.Pressure - log.Pressure) / 10) - 1;
            var time      = (lastLog.Timestamp - log.Timestamp).getTotalSeconds();

            if (time == 0) throw new Exception();

            return distance / time;
        }
    }
}
