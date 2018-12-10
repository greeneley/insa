using System;

namespace ExemplePlonge
{
    internal class Log
    {
        public double Pressure { get; private set; }
        public DateTime Timestamp { get; private set; }

        public Log(double pressure, DateTime timestamp)
        {
            this.Pressure  = pressure;
            this.Timestamp = timestamp;
        }

        public Log(double pressure)
        {
            this.Pressure  = pressure;
            this.Timestamp = DateTime.Now;
        }
    }
}