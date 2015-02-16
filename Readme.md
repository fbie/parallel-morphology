# Connected Set Filtering on Shared-Memory Multiprocessors #

This is the repository associated with my thesis. All you really need is located
in the **scripts** folder.

## Getting third-party dependencies ##

You require **perf** in order to record all performance metrics during
experiments. Furthermore, you will need **matplotlib** to plot the recorded
metrics.

Other than that, there are dependencies which you cannot get through your
package manager. Therfore I added some scripts which do that for you. All you'll
need is **mercurial** (hg) installed.

Then run first

``` $ sh scripts/get-multiverse.sh ```

to download the multiverse libraries. Then, run

``` $ sh scripts/clone-jpf.sh && sh scripts/build-jpf.sh ```

to first clone the JPF repositories (the script also applies a small patch that
fixes error reporting) and build them afterwards.

## Running experiments ##

In order to run performance experiments, run

``` $ scripts/test-area-opening.sh ```

in order to run area opening experiments and

``` $ scripts/test-union-find.sh ```

in order to run union-find performanc experiments. What is being executed
exactly is defined in the shell scripts themselves. The results will be stored
in the ```test``` folder as .csv files.

To plot results using [matplotlib](http://matplotlib.org/), run first

``` $ scripts/plot-csv.py test/<result-file>.csv -l```

to list all recorded metrics and plot a metric using e.g.

``` $ scripts/plot-csv.py test/<result-file>.csv --data ms --legend --baseline```

The ```--help``` switch lists the configuration possibilities for
**plot-csv.py**.

## Running JPF ##

A checkout of JPF modules is part of this repository for convenience. In order to run the verification for all algorithms (limited to one hour per algorithm), run

``` $ scripts/verify.sh ```

The results will be stored in the ```test``` folder as .log files.

## Third party code ##

The following third-party code has been made part of this repository for
convenience:

  * *jpf-core* ([Java Pathfinder](http://babelfish.arc.nasa.gov/trac/jpf) core module)
  * *jpf-aprop* (Property annotations for JPF)
  * *jpf-symb* (Symbolic execution of JPF)
  * *lib* ([Multiverse](http://multiverse.codehaus.org), a software transactional memory library)

**I do not claim authorship or ownership on those.** They are merely needed in
  order to execute and verify the code, which I wrote (i.e. contents of
  *parallel-union-find*, *parallel-morphology* and *parallel-experiments*).
