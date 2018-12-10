using System;
using System.Collections.Generic;
using System.Text;

namespace ExemplePlonge
{
    public class PressureObservable
    {
        List<ITickObserver> observers = new List<ITickObserver>();

        public void Register(ITickObserver observer)
        {
            if (!(this.observers.Contains(observer)))
            {
                this.observers.Add(observer);
            }
        }

        public void Unregister(ITickObserver observer)
        {
            if (this.observers.Contains(observer))
            {
                this.observers.Remove(observer);
            }
        }

        public void TriggerEvent(double pressure)
        {
            foreach(var observer in this.observers)
            {
                observer.Execute(pressure);
            }
        }
    }
}
