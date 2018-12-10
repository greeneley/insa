using System;
using System.Collections.Generic;
using System.Text;

namespace ExemplePlonge
{
    public interface IPressureObserver
    {
        bool Execute(double pressure);
    }
}
