using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Text;

namespace Server
{
    public class Response
    {
        public int Status { get; set; }
        public Dictionary<string, string> Headers { get; set; } = new Dictionary<string, string>();
        public JObject Payload { get; set; }
        public override string ToString()
        {
            return $"Status: {Status}, Payload: {Payload}";
        }
    }
}
