using System;

namespace ExemplePlonge
{
    class Program
    {
        static void Main(string[] args)
        {
            var logCollection      = new LogCollection();
            var tickManager        = new TickObservable();
            var pressureManager    = new PressureManager();
            var pressureObservable = new PressureObservable();
            var pressureLogWriter  = new DescentStrategy(logCollection);

            pressureManager.PressureObservable = pressureObservable;
            tickManager.Register(pressureManager);
            pressureObservable.Register(pressureLogWriter);

            do
            {
                var ms = DateTime.Now.ToString("fff").ToInt32();

                if(ms % 15 == 0)
                {
                    tickManager.TriggerEvent();
                }

            } while (true);
        }
    }
}
