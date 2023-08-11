#!/bin/bash

decrypt() {
  key="$1"
  text="$2"
  alphabet="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
  result=""

  klen=${#key}
  alen=${#alphabet}

  for (( i=0; i<${#text}; i++ )); do
    ch="${text:$i:1}"
    key_ch="${key:$(($i % $klen)):1}"

    ch_int=-1
    shift=-1

    for (( j=0; j<$alen; j++ )); do
      if [ "$ch" == "${alphabet:$j:1}" ]; then
        ch_int=$j
      fi
      if [ "$key_ch" == "${alphabet:$j:1}" ]; then
        shift=$j
      fi
    done

    if [ "$ch_int" -ge 0 ] && [ "$shift" -ge 0 ]; then
      shifted=$(( ($ch_int - $shift + $alen) % $alen ))
      result="$result${alphabet:$shifted:1}"
    else
      result="$result$ch"
    fi
  done

  echo -n "$result"
  echo ""
}

key="$1"
if [[ "$2" == "-f" ]]; then
  if [[ -f "$3" ]]; then
    text=$(<"$3")
  else
    echo "File not found: $3"
    exit 1
  fi
else
  text="$2"
fi
decrypt "$key" "$text"
