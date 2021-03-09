using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Text;

namespace Server
{
    public class Request
    {
        public string Method { get; set; }
        public string Path { get; set; }
        public Dictionary<string, string> Headers { get; set; }
        public JObject Payload { get; set; }

        public override string ToString()
        {
            return $"Method: {Method}, Path: {Path}, Payload: {Payload}";
        }
    }
}
