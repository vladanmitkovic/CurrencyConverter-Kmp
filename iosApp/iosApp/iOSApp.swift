import SwiftUI
import ComposeApp

@main
struct iOSApp: App { 
    
    init() {
        do {
            try KoinHelper().doInitKoin()
        } catch {
            print("Failed to initialize Koin: \(error)")
        }
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
