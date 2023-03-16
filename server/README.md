# Readme

## Architecture
(Brainstorm)

Single responsibility. Injection. Things do not know about what they get, only "interfaces".

There are apps, like contacts...
http://localhost:8000/contacts/...

http://localhost.8000/ - List of missions and info
http://localhost.8000/mission<n> which is an app type with mission<n>/solution/<solution>

???????? Ring router? ... use Compojure

How does app mechanism work?

App should have a clojure interface which can be used to wrap in API interface.

Function contacts/app will take a ressource map {:app "contacts" :fun "entry" :parameters [x y z]}
Result is a string or nil

Requests are converted to form:
{:app "contacts" :fun "entry" :param1 "x" :param2 5}

Register hackerio.core.apps.contacts/app as {"contacts" hackerio.core.apps.contacts/app}

apploader should be called to load apps
