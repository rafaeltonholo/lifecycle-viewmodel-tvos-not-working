import SwiftUI
import Shared

class ObservableState: ObservableObject {
    private var stateFlow: KmpStateFlow<UiState>? = nil
    @Published var value: UiState = UiState.Loading()
    
    private var closable: Closable? = nil
    init(stateFlow: KmpStateFlow<UiState>) {
        self.stateFlow = stateFlow
    }
    
    deinit {
        self.closable?.close()
    }
    
    func watch() {
        self.closable = stateFlow?.observe({ uiState in
            self.value = uiState
        })
    }
}

struct ContentView: View {
    @State private var viewModel = MyViewModel()
    @ObservedObject private var state: ObservableState

    @State private var showContent = false
    init(viewModel: MyViewModel = MyViewModel(), showContent: Bool = false) {
        self.viewModel = viewModel
        self.state = ObservableState(stateFlow: viewModel.state)
        self.showContent = showContent
    }

    var body: some View {
        VStack {
            Text("Current state: \(state.value)")
            Button("Click me!") {
                withAnimation {
                    showContent = !showContent
                }
            }

            if showContent {
                VStack(spacing: 16) {
                    Image(systemName: "swift")
                        .font(.system(size: 200))
                        .foregroundColor(.accentColor)
                    Text("SwiftUI: \(Greeting().greet())")
                }
                .transition(.move(edge: .top).combined(with: .opacity))
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .padding()
        .task {
            self.state.watch()
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
