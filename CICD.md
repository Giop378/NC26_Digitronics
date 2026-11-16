# CI/CD con GitHub Actions

## 1. Obiettivo

La pipeline definita in [`.github/workflows/ci.yml`](.github/workflows/ci.yml) automatizza la build e i test di Digitronics: a ogni modifica compila il progetto, esegue la suite di test e produce il WAR dell'applicazione, senza interventi manuali. Questo documento ne descrive gli stadi e spiega come garantisce la riproducibilità della build, la qualità del codice e la sicurezza della pipeline.

## 2. Stadi

### Quando parte

| Evento | Quando |
|---|---|
| `push` | su qualsiasi branch, `master` compreso (quindi anche dopo ogni merge) |
| `pull_request` | verso `main`/`master` |
| `workflow_dispatch` | avvio manuale dalla tab Actions |

### Il job `build-and-test`

Il workflow ha un solo job, eseguito su un runner `ubuntu-latest`:

1. **Checkout** (`actions/checkout@v5`): scarica il codice al commit da verificare.
2. **Set up JDK 21** (`actions/setup-java@v4`): installa Eclipse Temurin 21 e ripristina la cache delle dipendenze Maven.
3. **Build and run tests** (`./mvnw -B clean verify`): compila, esegue unit test (Mockito) e test di integrazione (database H2 in memoria) e crea il WAR. Se un test fallisce, la pipeline si ferma.
4. **Upload test reports** (`if: always()`): carica i report di Surefire, anche quando un test fallisce.
5. **Upload WAR** (`if: success()`): carica il WAR come artifact `digitronics-<SHA del commit>`, conservato per 30 giorni, solo se i passi precedenti hanno avuto successo.

## 3. Proprietà di dependability garantite

### Riproducibilità della build

- **Ambiente effimero e isolato** (*ephemeral environment*): ogni esecuzione parte da una macchina virtuale pulita, quindi il risultato non dipende dall'ambiente di chi ha scritto il codice ed è eliminato il problema del "funziona sulla mia macchina".
- **Versioni fissate** (*pinned action versions*): JDK e action hanno versioni esplicite (`@v5`, `@v4`), quindi un aggiornamento esterno non può cambiare la build senza una modifica al workflow.
- **Maven Wrapper**: `./mvnw` usa la versione di Maven indicata nel repository, identica in locale e in CI.
- **Cache delle dipendenze** (*build caching*): con `cache: maven` le dipendenze non vengono riscaricate a ogni esecuzione; la cache è legata al `pom.xml` e si rigenera quando le dipendenze cambiano.
- **Artifact immutabili e tracciabili** (*immutability*): il WAR viene costruito una sola volta, nella stessa esecuzione in cui i test sono passati, e porta nel nome lo SHA del commit da cui è stato prodotto. La conservazione di 30 giorni evita di accumulare artifact vecchi.

### Qualità del codice

- **Verifica automatica e feedback rapido** (*automated verification & fast feedback*): i test vengono eseguiti a ogni push e su ogni pull request, così un errore emerge subito, sul branch in cui è stato introdotto. Nella pull request l'esecuzione verifica il risultato dell'unione con `master`; per regola del gruppo il merge avviene solo con la CI verde e dopo la revisione di un altro componente.
- **Pubblicazione solo se tutto passa**: con `if: success()` il WAR viene caricato solo se build e test hanno avuto successo.
- **Report sempre disponibili**: con `if: always()` la causa di un fallimento si legge dalla pagina dell'esecuzione, senza riprodurlo in locale.
- **Timeout del job**: con `timeout-minutes: 15` un test bloccato fa fallire il job in pochi minuti invece di occupare il runner fino a 6 ore.
- **Avvio manuale**: `workflow_dispatch` permette di rilanciare la pipeline senza un nuovo commit, per esempio per verificare un errore occasionale.

### Sicurezza della pipeline

- **Privilegio minimo** (*least privilege*): il `GITHUB_TOKEN` ha solo il permesso `contents: read`, quindi anche un'action compromessa non potrebbe modificare il repository o le pull request.
- **Nessun segreto in chiaro** (*secrets isolation*): il workflow non contiene password, chiavi o token, perché i test non richiedono credenziali; se in futuro serviranno, verranno lette dai GitHub Secrets (`${{ secrets.NOME }}`).
