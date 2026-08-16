#!/usr/bin/env bash
set -euo pipefail

loader="${1:?Usage: smoke-run.sh <fabric|forge|neoforge> <client|server>}"
side="${2:?Usage: smoke-run.sh <fabric|forge|neoforge> <client|server>}"

if [[ "$loader" != "fabric" && "$loader" != "forge" && "$loader" != "neoforge" ]]; then
  echo "Unsupported loader: $loader" >&2
  exit 2
fi

if [[ "$side" != "client" && "$side" != "server" ]]; then
  echo "Unsupported side: $side" >&2
  exit 2
fi

run_directory="$loader/runs/$side"
latest_log="$run_directory/logs/latest.log"
console_log="${RUNNER_TEMP:-/tmp}/aimassistancemod-$loader-$side.log"
task=":$loader:run${side^}"
fatal_pattern='MixinApplyError|MixinTransformerError|Critical injection failure|ModLoadingCrashException|has failed to load correctly|Failed to initialize mod containers|mods that were not found|Mod Loading has failed|Failed to create window|Missing metadata in pack mod:motorassistancemod|failed to load a valid resourcePackInfo|Exception in thread "(main|Render thread|Server thread)"|This crash report has been saved to'

mkdir -p "$run_directory"
rm -f "$latest_log" "$console_log"

if [[ "$side" == "server" ]]; then
  printf 'eula=true\n' > "$run_directory/eula.txt"
  printf 'online-mode=false\nserver-port=0\n' > "$run_directory/server.properties"
  success_pattern='Done \('
  command=(./gradlew "$task" --console=plain)
else
  success_pattern='Created: .*minecraft:textures/atlas/gui\.png-atlas'
  command=(xvfb-run -a ./gradlew "$task" --console=plain)
fi

setsid "${command[@]}" > "$console_log" 2>&1 &
process_id=$!

cleanup() {
  kill -- "-$process_id" 2>/dev/null || true
  wait "$process_id" 2>/dev/null || true
}
trap cleanup EXIT

deadline=$((SECONDS + 180))
while (( SECONDS < deadline )); do
  if [[ -f "$latest_log" ]]; then
    if grep -Eq "$fatal_pattern" "$latest_log"; then
      echo "Fatal mod-loading error detected during $loader $side smoke test" >&2
      cat "$latest_log" >&2
      exit 1
    fi

    if grep -Eq "$success_pattern" "$latest_log"; then
      echo "$loader $side smoke test reached its startup marker"
      exit 0
    fi
  fi

  if ! kill -0 "$process_id" 2>/dev/null; then
    echo "$loader $side exited before reaching its startup marker" >&2
    cat "$console_log" >&2
    [[ ! -f "$latest_log" ]] || cat "$latest_log" >&2
    exit 1
  fi

  sleep 2
done

echo "$loader $side did not finish starting within 180 seconds" >&2
cat "$console_log" >&2
[[ ! -f "$latest_log" ]] || cat "$latest_log" >&2
exit 1
