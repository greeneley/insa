using System;
using System.Collections.Generic;
using System.Text;

namespace ExemplePlonge
{
    public class PressureManager : ITickObserver
    {
        private Sensor            sensor = new Sensor();
        public PressureObservable PressureObservable { get; set; }

        private async void ReadPressure()
        {
            double pressure = await this.sensor.ReadAsync();
            this.PressureObservable.TriggerEvent(pressure);
        }

        private void Execute()
        {
            this.ReadPressure();
        }
    }
}
