#!/usr/bin/env bash
# 📝 Notes App - Unified Launcher (backend + frontend)
set -e

# ────────────────────────────────────────────────────────────
#  ANSI colours
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'
echo_c() { printf "${1}${2}${NC}\n"; }
header() {
  echo ""
  echo_c "$BLUE" "=================================="
  echo_c "$BLUE" "  $1"
  echo_c "$BLUE" "=================================="
  echo ""
}

# ────────────────────────────────────────────────────────────
# 1) Clean previous processes
clean_up() {
  echo_c "$YELLOW" "🧹 Cleaning up previous processes…"
  pkill -f "spring-boot:run"   2>/dev/null || true
  pkill -f "ng serve"          2>/dev/null || true
  pkill -f "npm start"         2>/dev/null || true
  rm -f backend.pid frontend.pid backend.log frontend.log
  sleep 1
}

# ────────────────────────────────────────────────────────────
# 2) Tool checks
check_tools() {
  header "🔍 Checking required tools"

  local missing=0
  command -v java >/dev/null && \
    echo_c "$GREEN" "✅ Java: $(java -version 2>&1 | head -n1)" || { echo_c "$RED" "❌ Java missing"; missing=1; }
  command -v mvn  >/dev/null && \
    echo_c "$GREEN" "✅ Maven found"                          || { echo_c "$RED" "❌ Maven missing";  missing=1; }
  command -v node >/dev/null && \
    echo_c "$GREEN" "✅ Node.js: $(node -v)"                  || { echo_c "$RED" "❌ Node.js missing"; missing=1; }
  command -v npm  >/dev/null && \
    echo_c "$GREEN" "✅ npm: v$(npm -v)"                      || { echo_c "$RED" "❌ npm missing";    missing=1; }

  if [[ $missing -eq 1 ]]; then
    echo_c "$RED" "Install the missing tools and re-run."
    exit 1
  fi
}

# ────────────────────────────────────────────────────────────
# 3) Folder structure checks
check_structure() {
  header "📁 Checking folder structure"
  [[ -d backend/notes-app ]]  || { echo_c "$RED" "❌ backend/notes-app directory missing";  ls -la; exit 1; }
  [[ -d frontend/notes-app ]] || { echo_c "$RED" "❌ frontend/notes-app directory missing"; ls -la; exit 1; }
  echo_c "$GREEN" "✅ Structure OK"
}

# ────────────────────────────────────────────────────────────
# 4) Backend setup & start
setup_backend() {
  header "☕ Setting up backend"
  pushd backend/notes-app >/dev/null
  echo_c "$BLUE" "🔄 Maven clean & compile..."
  mvn -q clean compile
  popd >/dev/null
}

start_backend() {
  header "🚀 Starting backend"
  pushd backend/notes-app >/dev/null
  nohup mvn -q spring-boot:run > ../../backend.log 2>&1 &
  echo $! > ../../backend.pid
  popd >/dev/null

  echo_c "$BLUE" "⏳ Waiting for backend (8080)…"
  for _ in {1..30}; do
    curl -s http://localhost:8080/actuator/health >/dev/null && {
      echo_c "$GREEN" "✅ Backend ready → http://localhost:8080"
      return
    }
    sleep 2
    printf "."
  done
  echo_c "$YELLOW" "\n⚠️ Backend still starting; check logs with: tail -f backend.log"
}

# ────────────────────────────────────────────────────────────
# 5) Frontend setup & start
setup_frontend() {
  header "🅰️ Setting up frontend"
  pushd frontend/notes-app >/dev/null
  echo_c "$BLUE" "🔄 npm install…"
  npm install --silent
  popd >/dev/null
}

start_frontend() {
  header "🎨 Starting frontend"
  pushd frontend/notes-app >/dev/null
  nohup npm start > ../../frontend.log 2>&1 &
  echo $! > ../../frontend.pid
  popd >/dev/null

  echo_c "$BLUE" "⏳ Waiting for frontend (4200)…"
  for _ in {1..20}; do
    curl -s http://localhost:4200 >/dev/null && {
      echo_c "$GREEN" "✅ Frontend ready → http://localhost:4200"
      return
    }
    sleep 3
    printf "."
  done
  echo_c "$YELLOW" "\n⚠️ Frontend still starting; check logs with: tail -f frontend.log"
}

# ────────────────────────────────────────────────────────────
# 6) Final info
final_info() {
  header "🎉 Application is up!"
  echo_c "$GREEN" "🌐 URLs:"
  echo_c "$BLUE"  "  • Frontend → http://localhost:4200"
  echo_c "$BLUE"  "  • Backend  → http://localhost:8080"
  echo_c "$BLUE"  "  • API Docs → http://localhost:8080/swagger-ui.html"
  echo_c "$YELLOW" "\nLogs:"
  echo_c "$BLUE"  "  tail -f backend.log"
  echo_c "$BLUE"  "  tail -f frontend.log"
  echo_c "$GREEN" "\n✨ Have fun!"
}

# ────────────────────────────────────────────────────────────
#  Main dispatcher
main() {
  case "${1:-all}" in
    clean)
      clean_up
      echo_c "$GREEN" "✔ Clean complete"
      ;;
    backend)
      clean_up; check_tools; check_structure
      setup_backend; start_backend
      ;;
    frontend)
      clean_up; check_tools; check_structure
      setup_frontend; start_frontend
      ;;
    all|"")
      header "📝 NOTES APP – Simple Launcher"
      clean_up; check_tools; check_structure
      setup_backend; setup_frontend
      start_backend; start_frontend
      final_info
      ;;
    help|*)
      echo_c "$BLUE" "Usage: ./run.sh [all|backend|frontend|clean|help]"
      exit 0
      ;;
  esac
}

trap 'echo_c "$YELLOW" "\n🛑 Interrupted. Cleaning up…"; clean_up; exit 1' INT
main "$@"
